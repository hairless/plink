package com.github.hairless.plink.service.transform;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobClientVersionEnum;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.enums.JobTypeEnum;
import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.service.FlinkClusterService;
import com.github.hairless.plink.service.factory.FlinkClusterServiceFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: silence
 * @date: 2020/1/27
 */
@Service
public class JobTransform implements Transform<JobDTO, Job> {
    @Autowired
    private FlinkClusterServiceFactory flinkClusterServiceFactory;

    @Override
    public JobDTO transform(Job job) {
        if (job == null) {
            return null;
        }
        JobDTO jobDTO = new JobDTO();
        BeanUtils.copyProperties(job, jobDTO);
        //setConfig
        if (jobDTO.getConfigJson() != null) {
            jobDTO.setConfig(JSON.parseObject(jobDTO.getConfigJson(), FlinkConfig.class));
        } else {
            jobDTO.setConfig(new FlinkConfig());
        }
        //setLastStatusDesc
        JobInstanceStatusEnum statusEnum = JobInstanceStatusEnum.getEnum(job.getLastStatus());
        if (statusEnum != null) {
            jobDTO.setLastStatusDesc(statusEnum.getDesc());
        }
        //setTypeDesc
        JobTypeEnum jobTypeEnum = JobTypeEnum.getEnum(job.getType());
        if (jobTypeEnum != null) {
            jobDTO.setTypeDesc(jobTypeEnum.getDesc());
        }
        //setClientVersionDesc
        JobClientVersionEnum versionEnum = JobClientVersionEnum.getEnum(job.getClientVersion());
        if (versionEnum != null) {
            jobDTO.setClientVersionDesc(versionEnum.getDesc());
        }
        //setLastUiAddress
        if (job.getLastAppId() != null) {
            FlinkClusterService defaultFlinkClusterService = flinkClusterServiceFactory.getDefaultFlinkClusterService();
            try {
                JobInstanceDTO jobInstanceDTO = new JobInstanceDTO();
                jobInstanceDTO.setAppId(job.getLastAppId());
                jobInstanceDTO.setStatus(job.getLastStatus());
                jobDTO.setLastUiAddress(defaultFlinkClusterService.getJobUiAddress(jobInstanceDTO));
            } catch (Exception e) {
                throw new PlinkRuntimeException(e);
            }
        }
        //设置权限
        JobDTO.AuthMap authMap = new JobDTO.AuthMap();
        JobInstanceStatusEnum jobInstanceStatusEnum = JobInstanceStatusEnum.getEnum(job.getLastStatus());
        if (jobInstanceStatusEnum == null || jobInstanceStatusEnum.isFinalState()) {
            authMap.setEdit(true);
            authMap.setDelete(true);
            authMap.setStart(true);
        }
        if (JobInstanceStatusEnum.RUNNING.equals(jobInstanceStatusEnum)) {
            authMap.setStop(true);
            authMap.setRestart(true);
        }
        jobDTO.setAuthMap(authMap);

        return jobDTO;
    }

    @Override
    public Job inverseTransform(JobDTO dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getConfig() != null) {
            dto.setConfigJson(JSON.toJSONString(dto.getConfig()));
        }
        return dto;
    }
}
