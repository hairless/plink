package com.github.hairless.plink.schedule.task;

import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author: silence
 * @date: 2020/10/27
 */
@Slf4j
@Component
public class RetryJobTask {
    @Autowired
    private JobService jobService;

    @Async("commonThreadExecutor")
    public void asyncRetryJobTask(JobDTO jobDTO) {
        jobService.startJob(jobDTO);
    }
}
