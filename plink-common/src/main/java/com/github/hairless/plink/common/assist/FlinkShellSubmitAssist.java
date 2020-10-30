package com.github.hairless.plink.common.assist;

import com.github.hairless.plink.common.builder.FlinkShellCommandBuilder;
import com.github.hairless.plink.common.builder.JobBuilder;
import com.github.hairless.plink.common.factory.JobBuilderFactory;
import com.github.hairless.plink.common.util.FileUtil;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobTypeEnum;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.hairless.plink.common.util.MessageFormatUtil.format;

/**
 * @author: silence
 * @date: 2020/1/19
 */
@Slf4j
public class FlinkShellSubmitAssist {
    private final FlinkShellCommandBuilder flinkShellCommandBuilder;
    private final String appIdRegex;
    private final Pattern compile;

    public FlinkShellSubmitAssist(FlinkShellCommandBuilder flinkShellCommandBuilder, String appIdRegex) {
        this.flinkShellCommandBuilder = flinkShellCommandBuilder;
        this.appIdRegex = appIdRegex;
        compile = Pattern.compile(appIdRegex);
    }

    public String submitJob(JobInstanceDTO jobInstanceDTO, String logFile) throws Exception {
        JobTypeEnum jobTypeEnum = JobTypeEnum.getEnum(jobInstanceDTO.getJob().getType());
        JobBuilder jobBuilder = JobBuilderFactory.create(jobTypeEnum);
        FlinkSubmitOptions flinkSubmitOptions = jobBuilder.buildFlinkSubmitOption(jobInstanceDTO);
        String runCommand = flinkShellCommandBuilder.buildRunCommand(flinkSubmitOptions);
        String command = format("{0} >> {1} 2>&1", runCommand, logFile);
        log.debug("command:{}", command);
        log.info("jobInstance {} logging to file {}", jobInstanceDTO.getId(), logFile);
        int exitCode = syncExecShellCommand(command);
        if (exitCode != 0) {
            throw new PlinkMessageException("submit job failed! exitCode is " + exitCode);
        }
        String log = FileUtil.readFileToString(logFile);
        Matcher matcher = compile.matcher(log);
        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }
        return null;
    }

    public static int syncExecShellCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/sh", "-c", command);
        Process process = processBuilder.start();
        return process.waitFor();
    }
}
