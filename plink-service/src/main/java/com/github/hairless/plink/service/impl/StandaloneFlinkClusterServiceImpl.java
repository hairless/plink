package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.Assist.FlinkShellSubmitAssist;
import com.github.hairless.plink.common.builder.StandaloneCommandBuilder;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.rpc.FlinkRestRpcService;
import com.github.hairless.plink.service.FlinkClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: silence
 * @date: 2020/1/19
 */
@Component("standaloneFlinkClusterServiceImpl")
public class StandaloneFlinkClusterServiceImpl implements FlinkClusterService {
    @Autowired
    private FlinkRestRpcService flinkRestRpcService;

    private FlinkShellSubmitAssist flinkShellSubmitAssist =
            new FlinkShellSubmitAssist(StandaloneCommandBuilder.INSTANCE, "Job has been submitted with JobID ([a-zA-Z0-9]+)");

    @Override
    public String submitJob(JobInstanceDTO jobInstanceDTO, String logFile) throws Exception {
        return flinkShellSubmitAssist.submitJob(jobInstanceDTO, logFile);
    }

    @Override
    public JobInstanceStatusEnum jobStatus(JobInstanceDTO jobInstanceDTO) throws Exception {
        String status = flinkRestRpcService.queryJobStatus(jobInstanceDTO.getAppId());
        if (status != null) {
            switch (status) {
                case "FINISHED": {
                    return JobInstanceStatusEnum.SUCCESS;
                }
                case "FAILED": {
                    return JobInstanceStatusEnum.RUN_FAILED;
                }
                case "RUNNING": {
                    return JobInstanceStatusEnum.RUNNING;
                }
            }
        }
        return JobInstanceStatusEnum.UNKNOWN;
    }

    @Override
    public void stopJob(JobInstanceDTO jobInstanceDTO) throws Exception {
        flinkRestRpcService.stopJob(jobInstanceDTO.getAppId());
    }

    @Override
    public String getJobUiAddress(String appId) throws Exception {
        return flinkRestRpcService.getJobUiAddress(appId);
    }
}
