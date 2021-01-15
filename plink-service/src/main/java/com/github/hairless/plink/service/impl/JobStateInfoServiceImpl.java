package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.dao.mapper.JobStateInfoMapper;
import com.github.hairless.plink.model.dto.JobStateInfoDTO;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.model.pojo.JobStateInfo;
import com.github.hairless.plink.model.req.PageReq;
import com.github.hairless.plink.service.JobStateInfoService;
import com.github.hairless.plink.service.transform.JobStateInfoTransform;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chaixiaoxue
 * @date 2021/1/14 18:25
 */
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
}
