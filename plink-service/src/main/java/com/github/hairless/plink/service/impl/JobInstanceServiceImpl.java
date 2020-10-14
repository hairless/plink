package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.util.FileUtil;
import com.github.hairless.plink.dao.mapper.JobInstanceMapper;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.model.req.PageReq;
import com.github.hairless.plink.service.JobInstanceService;
import com.github.hairless.plink.service.transform.JobInstanceTransform;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/21 10:18
 */
@Slf4j
@Service
public class JobInstanceServiceImpl implements JobInstanceService {
    @Value("${logging.instance.dir}")
    private String instanceLogDir;
    @Value("${logging.instance.pattern}")
    private String instanceLogPattern;

    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobInstanceMapper jobInstanceMapper;
    @Autowired
    private JobInstanceTransform jobInstanceTransform;


    @Override
    public PageInfo<JobInstanceDTO> queryJobInstances(JobInstanceDTO jobInstanceDTO, PageReq pageReq) {
        if (jobInstanceDTO == null) {
            jobInstanceDTO = new JobInstanceDTO();
        }
        PageHelper.startPage(pageReq.getPageNum(), pageReq.getPageSize());
        List<JobInstance> jobInstanceList = jobInstanceMapper.select(jobInstanceDTO);
        PageInfo<JobInstance> jobInstancePageInfo = new PageInfo<>(jobInstanceList);
        return jobInstanceTransform.pageInfoTransform(jobInstancePageInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateJobAndInstanceStatus(JobInstance jobInstance) {
        int jobInstanceRowCnt = jobInstanceMapper.updateByPrimaryKeySelective(jobInstance);
        if (jobInstanceRowCnt == 0) {
            throw new PlinkMessageException("update job instance status fail");
        }
        if (jobInstance.getJobId() == null) {
            throw new PlinkMessageException("update job instance status fail,jobId is null");
        }
        Job job = new Job();
        job.setId(jobInstance.getJobId());
        job.setLastStatus(jobInstance.getStatus());
        job.setLastAppId(jobInstance.getAppId());
        job.setLastStartTime(jobInstance.getStartTime());
        job.setLastStopTime(jobInstance.getStopTime());
        int jobRowCnt = jobMapper.updateByPrimaryKeySelective(job);
        if (jobRowCnt == 0) {
            throw new PlinkMessageException("update job status fail");
        }
    }

    @Override
    public String startLog(Long jobInstanceId) {
        JobInstance jobInstance = jobInstanceMapper.selectByPrimaryKey(jobInstanceId);
        if (jobInstance == null) {
            return null;
        }
        JobInstanceDTO jobInstanceDTO = jobInstanceTransform.transform(jobInstance);
        String startLogFilePath = getStartLogFilePath(jobInstanceDTO);
        try {
            return FileUtil.readFileToString(startLogFilePath);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new PlinkRuntimeException("read instance start log error", e);
        }
    }

    @Override
    public String getStartLogFilePath(JobInstanceDTO jobInstanceDTO) {
        return String.format(instanceLogDir + instanceLogPattern, jobInstanceDTO.getJobId(), jobInstanceDTO.getId());
    }
}
