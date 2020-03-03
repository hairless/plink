package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.UploadUtil;
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
@Component("standaloneFlinkClusterService")
public class StandaloneFlinkClusterService implements FlinkClusterService {
    @Autowired
    private FlinkRestRpcService flinkRestRpcService;

    @Override
    public String submitJob(JobInstanceDTO jobInstanceDTO) throws Exception {
        String jarPath = UploadUtil.getJobJarsPath() + jobInstanceDTO.getJobId() + "/" + jobInstanceDTO.getConfig().getJarName();
        String jarId = flinkRestRpcService.uploadJar(jarPath);
        FlinkRestRpcService.RunConfig runConfig = new FlinkRestRpcService.RunConfig();
        runConfig.setEntryClass(jobInstanceDTO.getConfig().getMainClass());
        runConfig.setProgramArgs(jobInstanceDTO.getConfig().getArgs());
        runConfig.setParallelism(jobInstanceDTO.getConfig().getParallelism());
        String appId = flinkRestRpcService.runJar(jarId, runConfig);
        flinkRestRpcService.deleteJar(jarId);
        return appId;
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
