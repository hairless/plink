package com.github.hairless.plink.service.transform;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
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

    @Override
    public JobInstanceDTO transform(JobInstance jobInstance) {
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

        if (jobInstance.getJobId() != null) {
            Job job = jobMapper.selectByPrimaryKey(jobInstance.getJobId());
            jobInstanceDTO.setJob(jobTransform.transform(job));
        }
        return jobInstanceDTO;
    }

    @Override
    public JobInstance inverseTransform(JobInstanceDTO dto) {
        if (dto.getConfig() != null) {
            dto.setConfigJson(JSON.toJSONString(dto.getConfig()));
        }
        return dto;
    }
}
