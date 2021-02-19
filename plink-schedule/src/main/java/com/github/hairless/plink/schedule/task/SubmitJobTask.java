package com.github.hairless.plink.schedule.task;

import com.github.hairless.plink.common.util.LoggerUtil;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.service.FlinkClusterService;
import com.github.hairless.plink.service.JobInstanceService;
import com.github.hairless.plink.service.factory.FlinkClusterServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: silence
 * @date: 2020/1/27
 */
@Slf4j
@Component
public class SubmitJobTask {

    @Autowired
    private FlinkClusterServiceFactory flinkClusterServiceFactory;
    @Autowired
    private JobInstanceService jobInstanceService;

    @Async("commonThreadExecutor")
    public void asyncSubmitJobTask(JobInstanceDTO jobInstanceDTO) {
        String logFile = jobInstanceService.getClientLogFilePath(jobInstanceDTO);
        LoggerUtil.registerThreadFileAppender(String.valueOf(jobInstanceDTO.getId()), logFile);

        log.info("prepare starting job instance, jobId={}, instanceId={}", jobInstanceDTO.getJobId(), jobInstanceDTO.getId());
        try {
            //修改实例和任务状态由 '待启动' 为 '启动中'
            JobInstance jobInstance2Starting = new JobInstance();
            jobInstance2Starting.setId(jobInstanceDTO.getId());
            jobInstance2Starting.setJobId(jobInstanceDTO.getJobId());
            jobInstance2Starting.setStatus(JobInstanceStatusEnum.STARTING.getValue());
            jobInstance2Starting.setStartTime(new Date());
            jobInstanceService.updateJobAndInstanceStatus(jobInstance2Starting);
            log.info("jobInstance is starting, jobId={}, instanceId={}", jobInstanceDTO.getJobId(), jobInstanceDTO.getId());

            JobInstance jobInstanceSubmitted = new JobInstance();
            jobInstanceSubmitted.setId(jobInstanceDTO.getId());
            jobInstanceSubmitted.setJobId(jobInstanceDTO.getJobId());
            String appId;
            try {
                //提交平台实例（flink job）到flink集群
                FlinkClusterService defaultFlinkClusterService = flinkClusterServiceFactory.getDefaultFlinkClusterService();
                appId = defaultFlinkClusterService.submitJob(jobInstanceDTO);
                if (StringUtils.isBlank(appId)) {
                    throw new PlinkMessageException("appId is empty");
                }
                jobInstanceSubmitted.setAppId(appId);
                //提交成功状态为 '运行中'
                jobInstanceSubmitted.setStatus(JobInstanceStatusEnum.RUNNING.getValue());
                log.info("start success!!!,jobInstance is running, jobId={}, instanceId={}", jobInstanceDTO.getJobId(), jobInstanceDTO.getId());
            } catch (Throwable e) {
                //提交失败状态为 '启动失败'
                jobInstanceSubmitted.setStatus(JobInstanceStatusEnum.START_FAILED.getValue());
                jobInstanceSubmitted.setStopTime(new Date());
                log.warn("jobInstance start fail, jobId={}, instanceId={}", jobInstanceDTO.getJobId(), jobInstanceDTO.getId(), e);
            }
            jobInstanceService.updateJobAndInstanceStatus(jobInstanceSubmitted);
        } catch (Exception e) {
            log.warn("submitJobTask failed, jobId={}, instanceId={}", jobInstanceDTO.getJobId(), jobInstanceDTO.getId(), e);
        }

        LoggerUtil.rescindAppender(String.valueOf(jobInstanceDTO.getId()));
    }
}
