package com.github.hairless.plink.schedule;

import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.schedule.task.RetryJobTask;
import com.github.hairless.plink.service.transform.JobTransform;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author: silence
 * @date: 2020/10/27
 */
@Slf4j
@Component
public class RetryJobSchedule {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobTransform jobTransform;
    @Autowired
    private RetryJobTask retryJobTask;

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 20 * 1000)
    public void submitJobSchedule() throws Exception {
        Job condition = new Job();
        condition.setLastStatus(JobInstanceStatusEnum.RUN_FAILED.getValue());
        condition.setIsRetry(true);
        List<Job> retryJobs = jobMapper.select(condition);
        Collection<JobDTO> retryJobDTOs = jobTransform.transform(retryJobs);
        if (CollectionUtils.isNotEmpty(retryJobDTOs)) {
            log.info("retryJobSchedule start");
            for (JobDTO jobDTO : retryJobDTOs) {
                retryJobTask.asyncRetryJobTask(jobDTO);
            }
        }
    }
}
