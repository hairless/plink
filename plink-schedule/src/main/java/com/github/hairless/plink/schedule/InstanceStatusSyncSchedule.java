package com.github.hairless.plink.schedule;

import com.github.hairless.plink.dao.mapper.JobInstanceMapper;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.schedule.task.InstanceStatusSyncTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 实例状态同步定时任务
 *
 * @author: silence
 * @date: 2020/1/27
 */
@Slf4j
@Component
public class InstanceStatusSyncSchedule {
    @Autowired
    private JobInstanceMapper jobInstanceMapper;
    @Autowired
    private InstanceStatusSyncTask instanceStatusSyncTask;

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000)
    public void instanceStatusSyncSchedule() throws Exception {
        log.info("instanceStatusSyncSchedule start");
        JobInstance condition = new JobInstance();
        condition.setStatus(JobInstanceStatusEnum.RUNNING.getValue());
        List<JobInstance> jobInstances = jobInstanceMapper.select(condition);
        for (JobInstance jobInstance : jobInstances) {
            instanceStatusSyncTask.asyncInstanceStatusSyncTask(jobInstance);
        }
    }
}
