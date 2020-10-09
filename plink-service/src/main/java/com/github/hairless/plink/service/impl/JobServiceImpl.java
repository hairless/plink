package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.util.UploadUtil;
import com.github.hairless.plink.common.util.ValidatorUtil;
import com.github.hairless.plink.dao.mapper.JobInstanceMapper;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.model.req.PageReq;
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
import java.io.IOException;
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
    public JobDTO addJob(JobDTO jobDTO) {
        Job job = jobTransform.inverseTransform(jobDTO);
        try {
            jobMapper.insertSelective(job);
        } catch (DuplicateKeyException e) {
            throw new PlinkMessageException("job name is duplicate");
        }
        return jobTransform.transform(jobDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteJob(Long jobId) {
        if (jobId == null) {
            throw new PlinkMessageException("jobId is null");
        }
        int rowCnt = jobMapper.deleteByPrimaryKey(jobId);
        if (rowCnt == 0) {
            throw new PlinkMessageException("delete job fail");
        }
        //级联删除任务对应的实例信息
        JobInstance jobInstance = new JobInstance();
        jobInstance.setJobId(jobId);
        jobInstanceMapper.delete(jobInstance);
    }

    @Override
    public void deleteJobs(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new PlinkMessageException("idList is empty");
        }
        idList.forEach(id -> jobMapper.deleteByPrimaryKey(id));
    }

    @Override
    public void updateJob(JobDTO jobDTO) {
        if (jobDTO == null) {
            throw new PlinkMessageException("job is null");
        }
        if (jobDTO.getId() == null) {
            throw new PlinkMessageException("jobId is null");
        }
        Job job = jobTransform.inverseTransform(jobDTO);
        int rowCnt = jobMapper.updateByPrimaryKeySelective(job);
        if (rowCnt == 0) {
            throw new PlinkMessageException("update job fail");
        }
    }

    @Override
    public JobDTO queryJob(Long jobId) {
        if (jobId == null) {
            throw new PlinkMessageException("jobId is null");
        }
        Job job = jobMapper.selectByPrimaryKey(jobId);
        if (job == null) {
            throw new PlinkMessageException("job not found");
        }
        return jobTransform.transform(job);
    }

    @Override
    public PageInfo<JobDTO> queryJobs(JobDTO jobDTO, PageReq pageReq) {
        if (jobDTO == null) {
            jobDTO = new JobDTO();
        }
        PageHelper.startPage(pageReq.getPageNum(), pageReq.getPageSize());
        List<Job> jobList = jobMapper.select(jobDTO);
        PageInfo<Job> jobPageInfo = new PageInfo<>(jobList);
        return jobTransform.pageInfoTransform(jobPageInfo);
    }

    @Override
    public void uploadJar(Long jobId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new PlinkMessageException("the file is empty");
        }
        String filename = file.getOriginalFilename();
        File uploadPath = new File(UploadUtil.getJobJarsPath(jobId));
        if (!uploadPath.exists()) {
            if (!uploadPath.mkdirs()) {
                throw new PlinkMessageException("make upload dir fail!");
            }
        }
        File targetFile = new File(uploadPath, filename);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            throw new PlinkRuntimeException("file upload fail!", e);
        }
    }

    @Override
    public List<String> jarList(Long jobId) {
        File uploadPath = new File(UploadUtil.getJobJarsPath(jobId));
        if (uploadPath.exists()) {
            String[] fileNames = uploadPath.list();
            if (fileNames != null) {
                return Arrays.asList(fileNames);
            }
        }
        return Collections.emptyList();
    }

    /**
     * 由于作业启动可能会花费较长时间，所以采用异步启动的方式
     * 后期做分布式拆分也比较方便，接口调用所在机器不一定是作业提交的机器
     * 插入待启动实例记录，等待异步提交任务吊起
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void startJob(Long jobId) {
        Job job = jobMapper.selectByPrimaryKey(jobId);
        if (job == null) {
            throw new PlinkMessageException("jobId is not exist");
        }
        JobDTO jobDTO = jobTransform.transform(job);
        ValidatorUtil.validate(jobDTO);
        if (jobDTO.getLastStatus() != null) {
            JobInstanceStatusEnum jobInstanceStatusEnum = JobInstanceStatusEnum.getEnum(jobDTO.getLastStatus());
            if (jobInstanceStatusEnum != null && !jobInstanceStatusEnum.isFinalState()) {
                throw new PlinkMessageException(jobInstanceStatusEnum.getDesc() + " status can not start");
            }
        }
        JobInstance jobInstance = new JobInstance();
        jobInstance.setJobId(job.getId());
        jobInstance.setFlinkConfigJson(job.getFlinkConfigJson());
        jobInstance.setExtraConfigJson(job.getExtraConfigJson());
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
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void startJobs(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new PlinkMessageException("idList is empty");
        }
        idList.forEach(this::startJob);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stopJob(Long jobId) {
        if (jobId == null) {
            throw new PlinkMessageException("jobId is null");
        }
        Job job = jobMapper.selectByPrimaryKey(jobId);
        if (job == null) {
            throw new PlinkMessageException("jobId is not exist");
        }
        Long lastInstanceId = job.getLastInstanceId();
        if (lastInstanceId == null) {
            throw new PlinkMessageException("this job no instance information");
        }
        if (!JobInstanceStatusEnum.RUNNING.equals(JobInstanceStatusEnum.getEnum(job.getLastStatus()))) {
            throw new PlinkMessageException("instance is not running");
        }
        JobInstance jobInstance = jobInstanceMapper.selectByPrimaryKey(lastInstanceId);
        if (jobInstance == null) {
            throw new PlinkMessageException("instance not found");
        }
        try {
            flinkClusterServiceFactory.getDefaultFlinkClusterService().stopJob(jobInstanceTransform.transform(jobInstance));
        } catch (Exception e) {
            throw new PlinkRuntimeException("stop job fail", e);
        }
        JobInstance stoppedJobInstance = new JobInstance();
        stoppedJobInstance.setId(jobInstance.getId());
        stoppedJobInstance.setJobId(jobInstance.getJobId());
        stoppedJobInstance.setStatus(JobInstanceStatusEnum.STOPPED.getValue());
        stoppedJobInstance.setStopTime(new Date());
        jobInstanceService.updateJobAndInstanceStatus(stoppedJobInstance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stopJobs(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new PlinkMessageException("idList is empty");
        }
        idList.forEach(this::stopJob);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reStartJob(Long jobId) {
        this.stopJob(jobId);
        this.startJob(jobId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reStartJobs(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new PlinkMessageException("idList is empty");
        }
        idList.forEach(this::reStartJob);
    }
}
