package com.github.hairless.plink.service.transform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
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
public class JobInstanceTransform implements Transform<JobInstanceDTO, JobInstance> {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobTransform jobTransform;
    @Autowired
    private FlinkClusterServiceFactory flinkClusterServiceFactory;

    @Override
    public JobInstanceDTO transform(JobInstance jobInstance) {
        if (jobInstance == null) {
            return null;
        }
        JobInstanceDTO jobInstanceDTO = new JobInstanceDTO();
        BeanUtils.copyProperties(jobInstance, jobInstanceDTO);
        if (jobInstanceDTO.getFlinkConfigJson() != null) {
            jobInstanceDTO.setFlinkConfig(JSON.parseObject(jobInstanceDTO.getFlinkConfigJson(), FlinkConfig.class));
        } else {
            jobInstanceDTO.setFlinkConfig(new FlinkConfig());
        }
        if (jobInstanceDTO.getExtraConfig() != null) {
            jobInstanceDTO.setExtraConfig(JSON.parseObject(jobInstanceDTO.getExtraConfigJson()));
        } else {
            jobInstanceDTO.setExtraConfig(new JSONObject());
        }
        //setLastStatusDesc
        JobInstanceStatusEnum statusEnum = JobInstanceStatusEnum.getEnum(jobInstance.getStatus());
        if (statusEnum != null) {
            jobInstanceDTO.setStatusDesc(statusEnum.getDesc());
        }

        if (jobInstance.getAppId() != null) {
            FlinkClusterService defaultFlinkClusterService = flinkClusterServiceFactory.getDefaultFlinkClusterService();
            try {
                jobInstanceDTO.setUiAddress(defaultFlinkClusterService.getJobUiAddress(jobInstanceDTO));
            } catch (Exception e) {
                throw new PlinkRuntimeException(e);
            }
        }

        if (jobInstance.getJobId() != null) {
            Job job = jobMapper.selectByPrimaryKey(jobInstance.getJobId());
            jobInstanceDTO.setJob(jobTransform.transform(job));
        }
        return jobInstanceDTO;
    }

    @Override
    public JobInstance inverseTransform(JobInstanceDTO dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getFlinkConfig() != null) {
            dto.setFlinkConfigJson(JSON.toJSONString(dto.getFlinkConfig()));
        }
        if (dto.getExtraConfig() != null) {
            dto.setExtraConfigJson(JSON.toJSONString(dto.getExtraConfig()));
        }
        return dto;
    }
}
