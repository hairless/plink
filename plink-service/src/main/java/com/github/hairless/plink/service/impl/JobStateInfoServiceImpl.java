package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.dao.mapper.JobStateInfoMapper;
import com.github.hairless.plink.model.dto.JobStateInfoDTO;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.model.pojo.JobStateInfo;
import com.github.hairless.plink.model.req.PageReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobStateInfoService;
import com.github.hairless.plink.service.transform.JobStateInfoTransform;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chaixiaoxue
 * @date 2021/1/14 18:25
 */
@Slf4j
@Service
public class JobStateInfoServiceImpl implements JobStateInfoService {

    @Autowired
    private JobStateInfoMapper jobStateInfoMapper;

    @Autowired
    private JobStateInfoTransform jobStateInfoTransform;

    @Override
    public PageInfo<JobStateInfoDTO> queryJobStateInfos(JobStateInfoDTO jobStateInfoDTO, PageReq pageReq) {
        if (jobStateInfoDTO == null) {
            jobStateInfoDTO = new JobStateInfoDTO();
        }
        PageHelper.startPage(pageReq.getPageNum(), pageReq.getPageSize());
        List<JobStateInfo> jobStateInfoList = jobStateInfoMapper.select(jobStateInfoDTO);
        PageInfo<JobStateInfo> jobStateInfoPageInfo = new PageInfo<>(jobStateInfoList);
        return jobStateInfoTransform.pageInfoTransform(jobStateInfoPageInfo);
    }

    @Override
    public JobStateInfoDTO queryJobStateInfo(Long jobStateInfoId) {
        JobStateInfo jobStateInfo = jobStateInfoMapper.selectByPrimaryKey(jobStateInfoId);
        if (jobStateInfo == null){
            throw new PlinkMessageException("the jobStateInfo is not exist");
        }
        return jobStateInfoTransform.transform(jobStateInfo);
    }

    @Override
    public JobStateInfoDTO addJobStateInfo(JobStateInfoDTO jobStateInfoDTO) {
        //check jobId or instanceId
        if (jobStateInfoDTO.getJobId() == null || jobStateInfoDTO.getInstanceId() == null){
            throw new PlinkMessageException("the jobStateInfoDTO id or instanceId is not exist");
        }
        JobStateInfo jobStateInfo = jobStateInfoTransform.inverseTransform(jobStateInfoDTO);
        try {
            jobStateInfoMapper.insertSelective(jobStateInfo);
        }catch (Exception e){
            log.info("add jobStateInfo is fail , {}",e);
        }
        return jobStateInfoTransform.transform(jobStateInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteJobStateInfo(Long jobStateInfoId) {
        if (jobStateInfoId == null){
            return new Result(ResultCode.SUCCESS,"the jobStateInfoId is null");
        }
        jobStateInfoMapper.deleteByPrimaryKey(jobStateInfoId);
        return new Result<>(ResultCode.SUCCESS);
    }
}
