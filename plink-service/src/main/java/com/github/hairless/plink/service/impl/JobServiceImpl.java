package com.github.hairless.plink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.github.hairless.plink.common.HttpUtil;
import com.github.hairless.plink.common.PageInfoUtil;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.req.JobReq;
import com.github.hairless.plink.model.resp.JobResp;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * job service
 *
 * @Author Trevor
 * @Create 2020/1/14 20:26
 */
@Slf4j
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;
    //注入系统环境，拿到yml文件
    @Autowired
    private Environment env;

    @Override
    public Result<JobResp> addJob(JobReq jobReq) {
        try {
            jobReq.transform();
            jobMapper.insertSelective(jobReq);
            return new Result<>(ResultCode.SUCCESS, new JobResp().transform(jobReq));
        } catch (Exception e) {
            log.warn("add job fail! job={}", JSON.toJSONString(jobReq), e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result deleteJob(Long jobId) {
        if (jobId == null) {
            return new Result(ResultCode.FAILURE, "jobId is null");
        }
        try {
            int rowCnt = jobMapper.deleteByPrimaryKey(jobId);
            if (rowCnt == 0) {
                return new Result(ResultCode.FAILURE, "delete job fail");
            }
            return new Result(ResultCode.SUCCESS);
        } catch (Exception e) {
            log.warn("delete job fail! jobId={}", jobId, e);
            return new Result(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result deleteJobs(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new Result(ResultCode.FAILURE, "idList is empty");
        }
        try {
            idList.forEach(id -> jobMapper.deleteByPrimaryKey(id));
            return new Result(ResultCode.SUCCESS);
        } catch (Exception e) {
            log.warn("delete job fail! idList={}", JSON.toJSONString(idList), e);
            return new Result(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result updateJob(JobReq jobReq) {
        if (jobReq == null) {
            return new Result(ResultCode.FAILURE, "job is null");
        }
        if (jobReq.getId() == null) {
            return new Result(ResultCode.FAILURE, "jobId is null");
        }
        try {
            jobReq.transform();
            int rowCnt = jobMapper.updateByPrimaryKeySelective(jobReq);
            if (rowCnt == 0) {
                return new Result(ResultCode.FAILURE, "update job fail");
            }
            return new Result(ResultCode.SUCCESS);
        } catch (Exception e) {
            log.warn("update job fail! job={}", JSON.toJSONString(jobReq), e);
            return new Result(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result<JobResp> queryJob(Long jobId) {
        if (jobId == null) {
            return new Result<>(ResultCode.FAILURE, "jobId is null");
        }
        try {
            Job job = jobMapper.selectByPrimaryKey(jobId);
            if (job == null) {
                return new Result<>(ResultCode.FAILURE, "jobnot found");
            }
            return new Result<>(ResultCode.SUCCESS, new JobResp().transform(job));
        } catch (Exception e) {
            log.warn("query job fail! jobId={}", jobId, e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result<PageInfo<JobResp>> queryJobs(JobReq jobReq) {
        if (jobReq == null) {
            jobReq = new JobReq();
        }
        PageHelper.startPage(jobReq.getPageNum(), jobReq.getPageSize());
        try {
            List<Job> jobList = jobMapper.select(jobReq);
            PageInfo<Job> jobPageInfo = new PageInfo<>(jobList);
            return new Result<>(ResultCode.SUCCESS, PageInfoUtil.pageInfoTransform(jobPageInfo, JobResp.class));
        } catch (Exception e) {
            log.warn("query jobs fail! jobReq={}", JSON.toJSONString(jobReq), e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result uploadJar(Long jobId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new Result(ResultCode.FAILURE, "上传的文件为空");
        }
        String filename = file.getOriginalFilename();
        try {
            String parentDir = System.getProperty("user.dir");
            File uploadPath = new File(parentDir + "/uploadJars/" + jobId);
            if (!uploadPath.exists()) {
                if (!uploadPath.mkdirs()) {
                    return new Result<>(ResultCode.FAILURE, "make upload dir fail!");
                }
            }
            File targetFile = new File(uploadPath, filename);
            file.transferTo(targetFile);
            return new Result<>(ResultCode.SUCCESS);
        } catch (Exception e) {
            log.warn("upload jar fail! fileName={}", filename, e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result<List<String>> jarList(Long jobId) {
        String parentDir = System.getProperty("user.dir");
        try {
            File uploadPath = new File(parentDir + "/uploadJars/" + jobId);
            if (uploadPath.exists()) {
                String[] fileNames = uploadPath.list();
                return new Result<>(ResultCode.SUCCESS, Arrays.asList(fileNames));
            }
            return new Result<>(ResultCode.SUCCESS, Collections.emptyList());
        } catch (Exception e) {
            log.warn("get jar list fail! jobId={}", jobId, e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }



    @Override
    public Result startJob(JSONObject jsonObject) {

        //flinkURL 例如：localhost:8081
        String flinkURL = "http://"+env.getProperty("flink.restful.ip")+":"+env.getProperty("flink.restful.port");
        //本地jar包存放路径
        String parentDir = System.getProperty("user.dir");
        String jarPath = parentDir + "/uploadJars/"+jsonObject.getString("jarName");

        //向flink平台提交jar
        String resJson = null;
        try {
            resJson = HttpUtil.sendFlinkJar(flinkURL, jarPath);
        } catch (HttpProcessException e) {
            return new Result<>(ResultCode.EXCEPTION, e);
        }
        JSONObject flinkRestRes = JSON.parseObject(resJson);
        if ( !flinkRestRes.getString("status").equals("success")) {
            return new Result(ResultCode.EXCEPTION,flinkRestRes.getString("status"));
        }
        String filename = flinkRestRes.getString("filename");
        String filenames [] = filename.split("/");
        //预留向mysql写表 id
        String id = filenames[filenames.length-1];

        String startHttpURL= flinkURL + "/v1/jars/"+id+"/run";

        jsonObject.remove("jarName");
        String senJobJsonRes = HttpUtil.sendPost(startHttpURL, jsonObject.toJSONString());

        JSONObject jsonRes = JSON.parseObject(senJobJsonRes);
        //预留向mysql写表 jobid
        String jobid = (String) jsonRes.getOrDefault("jobid","FAIL");
        if (jobid.equals("FAIL")){
            return new Result(ResultCode.FAILURE,jobid);
        }
        return new Result(ResultCode.SUCCESS,jobid);
    }

}
