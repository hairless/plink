package com.github.hairless.plink.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.common.PageInfoUtil;
import com.github.hairless.plink.dao.mapper.JobInstanceMapper;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.model.req.JobInstanceReq;
import com.github.hairless.plink.model.req.JobInstanceReq;
import com.github.hairless.plink.model.resp.JobInstanceResp;
import com.github.hairless.plink.model.resp.JobResp;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobInstanceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/21 10:18
 */
@Slf4j
@Service
public class JobInstanceServiceImpl implements JobInstanceService {
    @Autowired
    private JobInstanceMapper jobInstanceMapper;

    @Override
    public Result<PageInfo<JobInstanceResp>> queryJobInstances(JobInstanceReq JobInstanceReq) {
        if (JobInstanceReq == null) {
            JobInstanceReq = new JobInstanceReq();
        }
        PageHelper.startPage(JobInstanceReq.getPageNum(), JobInstanceReq.getPageSize());
        try {
            List<JobInstance> jobInstanceList = jobInstanceMapper.select(JobInstanceReq);
            PageInfo<JobInstance> jobInstancePageInfo = new PageInfo<>(jobInstanceList);
            return new Result<>(ResultCode.SUCCESS, PageInfoUtil.pageInfoTransform(jobInstancePageInfo, JobInstanceResp.class));
        } catch (Exception e) {
            log.warn("query jobs fail! JobInstanceReq={}", JSON.toJSONString(JobInstanceReq), e);
            return new Result<>(ResultCode.EXCEPTION, e);
        }
    }
}
