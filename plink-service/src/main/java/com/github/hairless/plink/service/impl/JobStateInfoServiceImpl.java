package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.dao.mapper.JobInstanceMapper;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.dao.mapper.JobStateInfoMapper;
import com.github.hairless.plink.model.dto.JobStateInfoDTO;
import com.github.hairless.plink.model.enums.ClusterModeEnum;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobInstanceMapper jobInstanceMapper;

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
        if(StringUtils.isBlank(jobStateInfoDTO.getExternalPath())){
            throw new PlinkMessageException("Checkpoint ExternalPath is null");
        }
        if(StringUtils.isBlank(jobStateInfoDTO.getJobName())){
            throw new PlinkMessageException("Job Name is null");
        }
        //Make sure state is idempotent
        JobStateInfo jobStateInfoParam = new JobStateInfo();
        jobStateInfoParam.setExternalPath(jobStateInfoDTO.getExternalPath());
        List<JobStateInfo> jobStateInfoList = jobStateInfoMapper.select(jobStateInfoParam);
        if(CollectionUtils.isNotEmpty(jobStateInfoList)){
            return jobStateInfoTransform.transform(jobStateInfoList.get(0));
        }
        //standalone
        if(ClusterModeEnum.STANDALONE.getDesc().equalsIgnoreCase(jobStateInfoDTO.getMode())){
            Boolean aBoolean = setStateJobIdInstanceId(jobStateInfoDTO);
            if(!aBoolean){
                throw new PlinkMessageException("Set State jobId and instanceId error .");
            }
        }
        JobStateInfo jobStateInfo = jobStateInfoTransform.inverseTransform(jobStateInfoDTO);
        try {
            jobStateInfoMapper.insertSelective(jobStateInfo);
        }catch (Exception e){
            log.info("add jobStateInfo is fail , {}",e);
        }
        return jobStateInfoTransform.transform(jobStateInfo);
    }

    /**
     * Set State JobId InstanceId
     * @param jobStateInfoDTO
     */
    private Boolean setStateJobIdInstanceId(JobStateInfoDTO jobStateInfoDTO){
        Job jobParam = new Job();
        if(jobStateInfoDTO.getJobName().startsWith("PLINK_SQL_")){
            jobParam.setName(jobStateInfoDTO.getJobName().substring(10));
        }
        jobParam.setName(jobStateInfoDTO.getJobName());
        List<Job> jobList = jobMapper.select(jobParam);
        if (CollectionUtils.isEmpty(jobList)) {
            return false;
        }
        Job job = jobList.get(0);
        jobStateInfoDTO.setJobId(job.getId());

        Example example = new Example(JobInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobId",job.getId());
        example.orderBy("id").desc();
        PageHelper.startPage(1, 1);
        List<JobInstance> jobInstanceList = jobInstanceMapper.selectByExample(example);
        PageInfo<JobInstance> jobInstancePageInfo = new PageInfo<>(jobInstanceList);
        List<JobInstance> list = jobInstancePageInfo.getList();
        if(CollectionUtils.isEmpty(list)){
            return false;
        }
        jobStateInfoDTO.setInstanceId(list.get(0).getId());
        return true;
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
