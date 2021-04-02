package com.github.hairless.plink.service.transform;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.hairless.plink.common.util.JsonUtil;
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

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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
        return transform(Collections.singletonList(job)).stream().findFirst().orElse(null);
    }

    @Override
    public Collection<JobDTO> transform(Collection<Job> pojoList) {
        FlinkClusterService defaultFlinkClusterService = flinkClusterServiceFactory.getDefaultFlinkClusterService();
        return pojoList.stream().map(job -> {
            if (job == null) {
                return null;
            }
            JobDTO jobDTO = new JobDTO();
            BeanUtils.copyProperties(job, jobDTO);
            //setFlinkConfig
            if (jobDTO.getFlinkConfigJson() != null) {
                jobDTO.setFlinkConfig(JsonUtil.parseObject(jobDTO.getFlinkConfigJson(), FlinkConfig.class));
            } else {
                jobDTO.setFlinkConfig(new FlinkConfig());
            }
            //setExtraConfig
            if (jobDTO.getFlinkConfigJson() != null) {
                jobDTO.setExtraConfig(JsonUtil.parseObject(jobDTO.getExtraConfigJson()));
            } else {
                jobDTO.setExtraConfig(JsonNodeFactory.instance.objectNode());
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
                try {
                    JobInstanceDTO jobInstanceDTO = new JobInstanceDTO();
                    jobInstanceDTO.setId(job.getLastInstanceId());
                    jobInstanceDTO.setJobId(job.getId());
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
        }).collect(Collectors.toList());
    }

    @Override
    public Job inverseTransform(JobDTO dto) {
        if (dto == null) {
            return null;
        }
        return inverseTransform(Collections.singletonList(dto)).stream().findFirst().orElse(null);

    }

    @Override
    public Collection<Job> inverseTransform(Collection<JobDTO> dtoList) {
        return dtoList.stream().map(dto -> {
            if (dto == null) {
                return null;
            }
            if (dto.getFlinkConfig() != null) {
                dto.setFlinkConfigJson(JsonUtil.toJSONString(dto.getFlinkConfig()));
            }
            if (dto.getExtraConfig() != null) {
                dto.setExtraConfigJson(JsonUtil.toJSONString(dto.getExtraConfig()));
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
