package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.builder.YarnCommandBuilder;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.service.FlinkClusterService;
import com.google.common.io.CharStreams;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;

import static com.github.hairless.plink.common.util.MessageFormatUtil.format;

/**
 * @author: silence
 * @date: 2020/1/19
 */
@Component("yarnFlinkClusterService")
public class YarnFlinkClusterService implements FlinkClusterService {

    @Override
    public String submitJob(JobInstanceDTO jobInstanceDTO, String logFile) throws Exception {
        FlinkSubmitOptions flinkSubmitOptions = new FlinkSubmitOptions();
        flinkSubmitOptions.setJobName(jobInstanceDTO.getJob().getName());
        flinkSubmitOptions.setFlinkConfig(jobInstanceDTO.getConfig());
        String runCommand = YarnCommandBuilder.buildRunCommand(flinkSubmitOptions);
        String[] cmd = new String[]{"/bin/sh", "-c", format("{0} >> {1} 2>&1", runCommand, logFile)};
        Process process = Runtime.getRuntime().exec(cmd);
        int exitCode = process.waitFor();
        if (exitCode < 0) {
            throw new PlinkMessageException("submit job failed!");
        }
        String log = CharStreams.toString(new InputStreamReader(process.getInputStream()));
        return null;
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
