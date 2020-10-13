package com.github.hairless.plink.schedule;

import com.github.hairless.plink.dao.mapper.JobInstanceMapper;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.schedule.task.SubmitJobTask;
import com.github.hairless.plink.service.transform.JobInstanceTransform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 作业异步提交定时任务
 * 扫描待启动的实例进行异步的提交
 *
 * @author: silence
 * @date: 2020/1/27
 */
@Slf4j
@Component
public class SubmitJobSchedule {
    @Autowired
    private JobInstanceMapper jobInstanceMapper;
    @Autowired
    private SubmitJobTask submitJobTask;
    @Autowired
    private JobInstanceTransform jobInstanceTransform;

    @Scheduled(fixedDelay = 5 * 1000, initialDelay = 10 * 1000)
    public void submitJobSchedule() throws Exception {
        log.info("submitJobSchedule start");
        JobInstance condition = new JobInstance();
        condition.setStatus(JobInstanceStatusEnum.WAITING_START.getValue());
        List<JobInstance> jobInstances = jobInstanceMapper.select(condition);
        Collection<JobInstanceDTO> jobInstanceDTOS = jobInstanceTransform.transform(jobInstances);
        for (JobInstanceDTO jobInstanceDTO : jobInstanceDTOS) {
            submitJobTask.asyncSubmitJobTask(jobInstanceDTO);
        }
    }
}
