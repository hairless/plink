package com.github.hairless.plink.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.common.UploadUtil;
import com.github.hairless.plink.common.ValidatorUtil;
import com.github.hairless.plink.dao.mapper.JobInstanceMapper;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.model.req.PageReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobInstanceService;
import com.github.hairless.plink.service.JobService;
import com.github.hairless.plink.service.factory.FlinkClusterServiceFactory;
import com.github.hairless.plink.service.transform.JobInstanceTransform;
import com.github.hairless.plink.service.transform.JobTransform;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
    private JobTransform jobTransform;
    @Autowired
    private JobInstanceMapper jobInstanceMapper;
    @Autowired
    private JobInstanceTransform jobInstanceTransform;
    @Autowired
    private FlinkClusterServiceFactory flinkClusterServiceFactory;
    @Autowired
    private JobInstanceService jobInstanceService;

    @Override
    public Result<JobDTO> addJob(JobDTO jobDTO) {
        try {
            Job job = jobTransform.inverseTransform(jobDTO);
            jobMapper.insertSelective(job);
            return new Result<>(ResultCode.SUCCESS, jobTransform.transform(jobDTO));
        } catch (DuplicateKeyException e) {
            return new Result<>(ResultCode.FAILURE, "job name is duplicate");
        } catch (Exception e) {
            log.warn("add job fail! job={}", JSON.toJSONString(jobDTO), e);
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
    public Result updateJob(JobDTO jobDTO) {
        if (jobDTO == null) {
            return new Result(ResultCode.FAILURE, "job is null");
        }
        if (jobDTO.getId() == null) {
            return new Result(ResultCode.FAILURE, "jobId is null");
        }
        try {
            Job job = jobTransform.inverseTransform(jobDTO);
            int rowCnt = jobMapper.updateByPrimaryKeySelective(job);
            if (rowCnt == 0) {
                return new Result(ResultCode.FAILURE, "update job fail");
            }
            return new Result(ResultCode.SUCCESS);
        } catch (Exception e) {
            log.warn("update job fail! job={}", JSON.toJSONString(jobDTO), e);
            return new Result(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result<JobDTO> queryJob(Long jobId) {
        if (jobId == null) {
            return new Result<>(ResultCode.FAILURE, "jobId is null");
        }
        try {
            Job job = jobMapper.selectByPrimaryKey(jobId);
            if (job == null) {
                return new Result<>(ResultCode.FAILURE, "jobnot found");
            }
            return new Result<>(ResultCode.SUCCESS, jobTransform.transform(job));
        } catch (Exception e) {
            log.warn("query job fail! jobId={}", jobId, e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result<PageInfo<JobDTO>> queryJobs(JobDTO jobDTO, PageReq pageReq) {
        if (jobDTO == null) {
            jobDTO = new JobDTO();
        }
        PageHelper.startPage(pageReq.getPageNum(), pageReq.getPageSize());
        try {
            List<Job> jobList = jobMapper.select(jobDTO);
            PageInfo<Job> jobPageInfo = new PageInfo<>(jobList);
            return new Result<>(ResultCode.SUCCESS, jobTransform.pageInfoTransform(jobPageInfo));
        } catch (Exception e) {
            log.warn("query jobs fail! jobDTO={}", JSON.toJSONString(jobDTO), e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Override
    public Result uploadJar(Long jobId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new Result(ResultCode.FAILURE, "the file is empty");
        }
        String filename = file.getOriginalFilename();
        try {
            File uploadPath = new File(UploadUtil.getJobJarsPath() + jobId);
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
        try {
            File uploadPath = new File(UploadUtil.getJobJarsPath() + jobId);
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
        JobDTO jobDTO = jobTransform.transform(job);
        ValidatorUtil.validate(jobDTO);
        if (jobDTO.getLastStatus() != null) {
            JobInstanceStatusEnum jobInstanceStatusEnum = JobInstanceStatusEnum.getEnum(jobDTO.getLastStatus());
            if (jobInstanceStatusEnum != null && !jobInstanceStatusEnum.isFinalState()) {
                return new Result(ResultCode.FAILURE, jobInstanceStatusEnum.getDesc() + " status can not start");
            }
        }
        JobInstance jobInstance = new JobInstance();
        jobInstance.setJobId(job.getId());
        jobInstance.setConfigJson(job.getConfigJson());
        jobInstance.setStatus(JobInstanceStatusEnum.WAITING_START.getValue());
        int rowCnt = jobInstanceMapper.insertSelective(jobInstance);
        if (rowCnt == 0) {
            throw new PlinkMessageException("insert job instance fail");
        }
        Job newJob = new Job();
        newJob.setLastInstanceId(jobInstance.getId());
        newJob.setId(jobId);
        newJob.setLastStatus(JobInstanceStatusEnum.WAITING_START.getValue());
        int jobUpdateRowCnt = jobMapper.updateByPrimaryKeySelective(newJob);
        if (jobUpdateRowCnt == 0) {
            throw new PlinkMessageException("update job status fail");
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
                throw new PlinkMessageException("start job fail jobId=" + id);
            }
        });
        return new Result<>(ResultCode.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result stopJob(Long jobId) {
        try {
            Job job = jobMapper.selectByPrimaryKey(jobId);
            if (job == null) {
                return new Result(ResultCode.FAILURE, "jobId is not exist");
            }
            Long lastInstanceId = job.getLastInstanceId();
            if (lastInstanceId == null) {
                return new Result(ResultCode.FAILURE, "this job no instance information");
            }
            if (!JobInstanceStatusEnum.RUNNING.equals(JobInstanceStatusEnum.getEnum(job.getLastStatus()))) {
                return new Result(ResultCode.FAILURE, "instance is not running");
            }
            JobInstance jobInstance = jobInstanceMapper.selectByPrimaryKey(lastInstanceId);
            if (jobInstance == null) {
                return new Result(ResultCode.FAILURE, "instance not found");
            }
            flinkClusterServiceFactory.getDefaultFlinkClusterService().stopJob(jobInstanceTransform.transform(jobInstance));
            JobInstance stoppedJobInstance = new JobInstance();
            stoppedJobInstance.setId(jobInstance.getId());
            stoppedJobInstance.setJobId(jobInstance.getJobId());
            stoppedJobInstance.setStatus(JobInstanceStatusEnum.STOPPED.getValue());
            stoppedJobInstance.setStopTime(new Date());
            jobInstanceService.updateJobAndInstanceStatus(stoppedJobInstance);
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
                throw new PlinkMessageException("stop job fail jobId=" + id);
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
                throw new PlinkMessageException("reStart job fail jobId=" + id);
            }
        });
        return new Result<>(ResultCode.SUCCESS);
    }
}
