package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.Assist.FlinkShellSubmitAssist;
import com.github.hairless.plink.common.builder.YarnCommandBuilder;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.service.FlinkClusterService;
import org.springframework.stereotype.Component;

/**
 * @author: silence
 * @date: 2020/1/19
 */
@Component("yarnFlinkClusterServiceImpl")
public class YarnFlinkClusterServiceImpl implements FlinkClusterService {
    private final FlinkShellSubmitAssist flinkShellSubmitAssist =
            new FlinkShellSubmitAssist(YarnCommandBuilder.INSTANCE, "Submitting application master (application_[0-9_]+)");

    @Override
    public String submitJob(JobInstanceDTO jobInstanceDTO, String logFile) throws Exception {
        return flinkShellSubmitAssist.submitJob(jobInstanceDTO, logFile);
    }

    @Override
    public JobInstanceStatusEnum jobStatus(JobInstanceDTO jobInstanceDTO) throws Exception {
        return null;
    }

    @Override
    public void stopJob(JobInstanceDTO jobInstanceDTO) throws Exception {
    }

    @Override
    public String getJobUiAddress(String appId) throws Exception {
        return null;
    }
}
