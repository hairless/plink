package com.github.hairless.plink.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.dao.mapper.JobInstanceMapper;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.model.req.PageReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobInstanceService;
import com.github.hairless.plink.service.transform.JobInstanceTransform;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/21 10:18
 */
@Slf4j
@Service
public class JobInstanceServiceImpl implements JobInstanceService {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobInstanceMapper jobInstanceMapper;
    @Autowired
    private JobInstanceTransform jobInstanceTransform;


    @Override
    public Result<PageInfo<JobInstanceDTO>> queryJobInstances(JobInstanceDTO jobInstanceDTO, PageReq pageReq) {
        if (jobInstanceDTO == null) {
            jobInstanceDTO = new JobInstanceDTO();
        }
        PageHelper.startPage(pageReq.getPageNum(), pageReq.getPageSize());
        try {
            List<JobInstance> jobInstanceList = jobInstanceMapper.select(jobInstanceDTO);
            PageInfo<JobInstance> jobInstancePageInfo = new PageInfo<>(jobInstanceList);
            return new Result<>(ResultCode.SUCCESS, jobInstanceTransform.pageInfoTransform(jobInstancePageInfo));
        } catch (Exception e) {
            log.warn("query jobs fail! jobInstanceDTO={}", JSON.toJSONString(jobInstanceDTO), e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateJobAndInstanceStatus(JobInstance jobInstance){
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
}
