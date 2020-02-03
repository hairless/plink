package com.github.hairless.plink.service.impl;

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
        String parentDir = System.getProperty("user.dir");
        String jarPath = parentDir + "/uploadJars/" + jobInstanceDTO.getJobId() + "/" + jobInstanceDTO.getConfig().getJarName();
        String jarId = flinkRestRpcService.uploadJar(jarPath);
        FlinkRestRpcService.RunConfig runConfig = new FlinkRestRpcService.RunConfig();
        runConfig.setEntryClass(jobInstanceDTO.getConfig().getMainClass());
        runConfig.setProgramArgs(jobInstanceDTO.getConfig().getArgs());
        runConfig.setParallelism(jobInstanceDTO.getConfig().getParallelism());
        return flinkRestRpcService.runJar(jarId, runConfig);
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
            }
        }
        return null;
    }

    @Override
    public Boolean stopJob(JobInstanceDTO jobInstanceDTO) throws Exception {
        return flinkRestRpcService.stopJob(jobInstanceDTO.getAppId());
    }
}
