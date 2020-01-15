package com.github.hairless.plink.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.req.JobReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    @Override
    public Result<Job> addJob(Job job) {
        try {
            jobMapper.insertUseGeneratedKeys(job);
            return new Result<>(ResultCode.SUCCESS, job);
        } catch (Exception e) {
            log.warn("add job fail! job={}", JSON.toJSONString(job), e);
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
    public Result updateJob(Job job) {
        if (job == null) {
            return new Result(ResultCode.FAILURE, "job is null");
        }
        if (job.getId() == null) {
            return new Result(ResultCode.FAILURE, "jobId is null");
        }
        try {
            int rowCnt = jobMapper.updateByPrimaryKey(job);
            if (rowCnt == 0) {
                return new Result(ResultCode.FAILURE, "update job fail");
            }
            return new Result(ResultCode.SUCCESS);
        } catch (Exception e) {
            log.warn("update job fail! job={}", JSON.toJSONString(job), e);
            return new Result(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result<Job> queryJob(Long jobId) {
        if (jobId == null) {
            return new Result<>(ResultCode.FAILURE, "jobId is null");
        }
        try {
            Job job = jobMapper.selectByPrimaryKey(jobId);
            return new Result<>(ResultCode.SUCCESS, job);
        } catch (Exception e) {
            log.warn("query job fail! jobId={}", jobId, e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result<PageInfo<Job>> queryJobs(JobReq jobReq) {
        if (jobReq == null) {
            return new Result<>(ResultCode.FAILURE, "jobReq is null");
        }
        PageHelper.startPage(jobReq.getPageNum(), jobReq.getPageSize());
        try {
            List<Job> jobList = jobMapper.select(jobReq);
            return new Result<>(ResultCode.SUCCESS, new PageInfo<>(jobList));
        } catch (Exception e) {
            log.warn("query jobs fail! jobReq={}", JSON.toJSONString(jobReq), e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result uploadJar(Long jobId, MultipartFile file) {
        if (file==null||file.isEmpty()){
            return new Result(ResultCode.FAILURE, "上传的文件为空");
        }
        String filename = file.getOriginalFilename();
        try {
            file.transferTo(new File("jar/" + jobId,filename));
            return new Result<>(ResultCode.SUCCESS);
        } catch (Exception e) {
            log.warn("upload jar fail! fileName={}", filename, e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

}
