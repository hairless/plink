package com.github.hairless.plink.service.transform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.service.FlinkClusterService;
import com.github.hairless.plink.service.factory.FlinkClusterServiceFactory;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return transform(Collections.singletonList(jobInstance)).stream().findFirst().orElse(null);
    }

    @Override
    public Collection<JobInstanceDTO> transform(Collection<JobInstance> pojoList) {
        if (CollectionUtils.isEmpty(pojoList)) {
            return Collections.emptyList();
        }

        //根据jobId批量查询，提升性能
        Example example = new Example(Job.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", pojoList.stream().map(JobInstance::getJobId).collect(Collectors.toList()));
        Map<Long, JobDTO> jobDTOMap = jobTransform.transform(jobMapper.selectByExample(example)).stream().collect(Collectors.toMap(JobDTO::getId, Function.identity()));

        FlinkClusterService defaultFlinkClusterService = flinkClusterServiceFactory.getDefaultFlinkClusterService();

        return pojoList.stream().map(jobInstance -> {
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
            if (jobInstanceDTO.getExtraConfigJson() != null) {
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
                try {
                    jobInstanceDTO.setUiAddress(defaultFlinkClusterService.getJobUiAddress(jobInstanceDTO));
                } catch (Exception e) {
                    throw new PlinkRuntimeException(e);
                }
            }

            if (jobInstance.getJobId() != null) {
                jobInstanceDTO.setJob(jobDTOMap.get(jobInstance.getJobId()));
            }
            return jobInstanceDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public JobInstance inverseTransform(JobInstanceDTO dto) {
        return inverseTransform(Collections.singletonList(dto)).stream().findFirst().orElse(null);
    }

    @Override
    public Collection<JobInstance> inverseTransform(Collection<JobInstanceDTO> dtoList) {
        return dtoList.stream().map(dto -> {
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
        }).collect(Collectors.toList());
    }
}
