package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.assist.FlinkShellSubmitAssist;
import com.github.hairless.plink.common.builder.YarnCommandBuilder;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.rpc.YarnClientRpcService;
import com.github.hairless.plink.service.FlinkClusterService;
import com.github.hairless.plink.service.JobInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author: silence
 * @date: 2020/1/19
 */
@Service("yarnFlinkClusterServiceImpl")
public class YarnFlinkClusterServiceImpl implements FlinkClusterService {
    @Autowired
    private YarnClientRpcService yarnClientRpcService;
    @Autowired
    private JobInstanceService jobInstanceService;
    @Value("${cluster.queue}")
    private String defaultQueue;

    private final FlinkShellSubmitAssist flinkShellSubmitAssist =
            new FlinkShellSubmitAssist(YarnCommandBuilder.INSTANCE, "Submitting application master (application_[0-9_]+)");

    @Override
    public String submitJob(JobInstanceDTO jobInstanceDTO) throws Exception {
        String logFile = jobInstanceService.getClientLogFilePath(jobInstanceDTO);
        if (StringUtils.isNotBlank(defaultQueue)) {
            jobInstanceDTO.getFlinkConfig().setQueue(defaultQueue);
        }
        return flinkShellSubmitAssist.submitJob(jobInstanceDTO, logFile);
    }

    @Override
    public JobInstanceStatusEnum jobStatus(JobInstanceDTO jobInstanceDTO) throws Exception {
        YarnApplicationState yarnApplicationState = yarnClientRpcService.getYarnApplicationState(jobInstanceDTO.getAppId());
        if (yarnApplicationState != null) {
            switch (yarnApplicationState) {
                case NEW:
                case NEW_SAVING:
                case ACCEPTED:
                case SUBMITTED: {
                    return JobInstanceStatusEnum.STARTING;
                }
                case FINISHED: {
                    return JobInstanceStatusEnum.SUCCESS;
                }
                case FAILED: {
                    return JobInstanceStatusEnum.RUN_FAILED;
                }
                case KILLED: {
                    return JobInstanceStatusEnum.STOPPED;
                }
                case RUNNING: {
                    return JobInstanceStatusEnum.RUNNING;
                }
            }
        }
        return JobInstanceStatusEnum.UNKNOWN;
    }

    @Override
    public void stopJob(JobInstanceDTO jobInstanceDTO) throws Exception {
        yarnClientRpcService.killApplication(jobInstanceDTO.getAppId());
    }

    @Override
    public String getJobUiAddress(JobInstanceDTO jobInstanceDTO) throws Exception {
        if (StringUtils.isBlank(jobInstanceDTO.getAppId())) {
            return null;
        }
        String resourceManagerAddress = yarnClientRpcService.getResourceManagerAddress();
        JobInstanceStatusEnum jobInstanceStatusEnum = JobInstanceStatusEnum.getEnum(jobInstanceDTO.getStatus());
        switch (jobInstanceStatusEnum) {
            case RUNNING:
            case STARTING: {
                return resourceManagerAddress + "/proxy/" + jobInstanceDTO.getAppId() + "/";
            }
            default: {
                return resourceManagerAddress + "/cluster/app/" + jobInstanceDTO.getAppId() + "/";
            }
        }
    }
}
