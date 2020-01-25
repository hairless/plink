package com.github.hairless.plink.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.common.PageInfoUtil;
import com.github.hairless.plink.common.ValidatorUtil;
import com.github.hairless.plink.dao.mapper.JobInstanceMapper;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.model.req.JobReq;
import com.github.hairless.plink.model.resp.JobResp;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobService;
import com.github.hairless.plink.service.factory.FlinkClusterServiceFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private JobInstanceMapper jobInstanceMapper;
    @Autowired
    private FlinkClusterServiceFactory flinkClusterServiceFactory;

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

    /**
     * 由于作业启动可能会花费较长时间，所以采用异步启动的方式
     * 后期做分布式拆分也比较方便，接口调用所在机器不一定是作业提交的机器
     * 插入待启动实例记录，等待异步提交任务吊起
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result startJob(Long jobId) {
        Job job = jobMapper.selectByPrimaryKey(jobId);
        if (job == null) {
            return new Result(ResultCode.FAILURE, "jobId is not exist");
        }
        JobResp jobResp = new JobResp().transform(job);
        ValidatorUtil.validate(jobResp);
        if (jobResp.getLastStatus() != null) {
            JobInstanceStatusEnum jobInstanceStatusEnum = JobInstanceStatusEnum.getEnum(jobResp.getLastStatus());
            if (jobInstanceStatusEnum != null && !jobInstanceStatusEnum.isFinalState()) {
                return new Result(ResultCode.FAILURE, jobInstanceStatusEnum.getDesc() + " status can not start");
            }
        }
        Job newJob = new Job();
        newJob.setId(jobId);
        newJob.setLastStatus(JobInstanceStatusEnum.WAITING_START.getValue());
        int jobUpdateRowCnt = jobMapper.updateByPrimaryKeySelective(newJob);
        if (jobUpdateRowCnt == 0) {
            throw new PlinkRuntimeException("update job status fail");
        }
        JobInstance jobInstance = new JobInstance();
        jobInstance.setJobId(job.getId());
        jobInstance.setConfigJson(job.getConfigJson());
        jobInstance.setStatus(JobInstanceStatusEnum.WAITING_START.getValue());
        int rowCnt = jobInstanceMapper.insertSelective(jobInstance);
        if (rowCnt == 0) {
            throw new PlinkRuntimeException("insert job instance fail");
        }
        return new Result<>(ResultCode.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result startJobs(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new Result(ResultCode.FAILURE, "idList is empty");
        }
        idList.forEach(id -> {
            Result result = this.startJob(id);
            if (!result.getSuccess()) {
                throw new PlinkRuntimeException("start job fail jobId=" + id);
            }
        });
        return new Result<>(ResultCode.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result stopJob(Long jobId) {
        Job job = jobMapper.selectByPrimaryKey(jobId);
        if (job == null) {
            return new Result(ResultCode.FAILURE, "jobId is not exist");
        }
        try {
            // TODO change cancelJob param as string jobId
            //flinkClusterServiceFactory.getDefaultFlinkClusterService().cancelJob(new JobResp().transform(job));
            Job newJob = new Job();
            newJob.setId(jobId);
            newJob.setLastStatus(JobInstanceStatusEnum.STOPPED.getValue());
            int jobUpdateRowCnt = jobMapper.updateByPrimaryKeySelective(newJob);
            if (jobUpdateRowCnt == 0) {
                throw new PlinkRuntimeException("update job status fail");
            }
            //todo update instance status
            return new Result<>(ResultCode.SUCCESS);
        } catch (Exception e) {
            log.warn("stop job fail! jobId={}", jobId, e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result stopJobs(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new Result(ResultCode.FAILURE, "idList is empty");
        }
        idList.forEach(id -> {
            Result result = this.stopJob(id);
            if (!result.getSuccess()) {
                throw new PlinkRuntimeException("stop job fail jobId=" + id);
            }
        });
        return new Result<>(ResultCode.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result reStartJob(Long jobId) {
        Result stopResult = this.stopJob(jobId);
        if (!stopResult.getSuccess()) {
            return stopResult;
        }
        Result startResult = this.startJob(jobId);
        if (!startResult.getSuccess()) {
            return startResult;
        }
        return new Result<>(ResultCode.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result reStartJobs(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new Result(ResultCode.FAILURE, "idList is empty");
        }
        idList.forEach(id -> {
            Result result = this.reStartJob(id);
            if (!result.getSuccess()) {
                throw new PlinkRuntimeException("reStart job fail jobId=" + id);
            }
        });
        return new Result<>(ResultCode.SUCCESS);
    }
}
