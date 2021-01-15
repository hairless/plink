package com.github.hairless.plink.service.transform;

import com.github.hairless.plink.model.dto.JobStateInfoDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.enums.JobStateInfoTypeEnum;
import com.github.hairless.plink.model.pojo.JobStateInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author chaixiaoxue
 * @date 2021/1/14 18:35
 */
@Service
public class JobStateInfoTransform implements Transform<JobStateInfoDTO, JobStateInfo> {

    @Override
    public JobStateInfoDTO transform(JobStateInfo pojo) {
        if (pojo == null) {
            return null;
        }
        return transform(Collections.singletonList(pojo)).stream().findFirst().orElse(null);
    }

    @Override
    public Collection<JobStateInfoDTO> transform(Collection<JobStateInfo> pojoList) {
        if (CollectionUtils.isEmpty(pojoList)) {
            return Collections.emptyList();
        }

        return pojoList.stream().map(jobStateInfo -> {
            JobStateInfoDTO jobStateInfoDTO =new JobStateInfoDTO();
            BeanUtils.copyProperties(jobStateInfo, jobStateInfoDTO);

            //typeDesc
            JobStateInfoTypeEnum typeEnum = JobStateInfoTypeEnum.getEnum(jobStateInfo.getType());
            if (typeEnum != null) {
                jobStateInfoDTO.setTypeDesc(typeEnum.getDesc());
            }
            return jobStateInfoDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public JobStateInfo inverseTransform(JobStateInfoDTO dto) {
        JobStateInfo jobStateInfo = new JobStateInfo();
        BeanUtils.copyProperties(dto, jobStateInfo);
        return inverseTransform(Collections.singletonList(dto)).stream().findFirst().orElse(null);
    }

    @Override
    public Collection<JobStateInfo> inverseTransform(Collection<JobStateInfoDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Collections.emptyList();
        }
        return dtoList.stream().map(dto -> {
            JobStateInfo jobStateInfo = new JobStateInfo();
            BeanUtils.copyProperties(dto, jobStateInfo);
            return jobStateInfo;
        }).collect(Collectors.toList());
    }
}
