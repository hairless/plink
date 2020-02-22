package com.github.hairless.plink.service.transform;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.pojo.Job;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author: silence
 * @date: 2020/1/27
 */
@Service
public class JobTransform implements Transform<JobDTO, Job> {
    @Override
    public JobDTO transform(Job job) {
        JobDTO jobDTO = new JobDTO();
        BeanUtils.copyProperties(job, jobDTO);
        if (jobDTO.getConfigJson() != null) {
            jobDTO.setConfig(JSON.parseObject(jobDTO.getConfigJson(), FlinkConfig.class));
        } else {
            jobDTO.setConfig(new FlinkConfig());
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
        if (dto.getConfig() != null) {
            dto.setConfigJson(JSON.toJSONString(dto.getConfig()));
        }
        return dto;
    }
}
