package com.github.hairless.plink.service.transform;

import com.alibaba.fastjson.JSON;
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
        if (jobInstanceDTO.getConfigJson() != null) {
            jobInstanceDTO.setConfig(JSON.parseObject(jobInstanceDTO.getConfigJson(), FlinkConfig.class));
        } else {
            jobInstanceDTO.setConfig(new FlinkConfig());
        }
        //setLastStatusDesc
        JobInstanceStatusEnum statusEnum = JobInstanceStatusEnum.getEnum(jobInstance.getStatus());
        if (statusEnum != null) {
            jobInstanceDTO.setStatusDesc(statusEnum.getDesc());
        }

        if (jobInstance.getAppId() != null) {
            FlinkClusterService defaultFlinkClusterService = flinkClusterServiceFactory.getDefaultFlinkClusterService();
            try {
                jobInstanceDTO.setUiAddress(defaultFlinkClusterService.getJobUiAddress(jobInstance.getAppId()));
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
        if (dto.getConfig() != null) {
            dto.setConfigJson(JSON.toJSONString(dto.getConfig()));
        }
        return dto;
    }
}
