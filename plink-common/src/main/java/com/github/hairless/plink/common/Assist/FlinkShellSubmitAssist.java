package com.github.hairless.plink.common.Assist;

import com.github.hairless.plink.common.builder.ShellCommandBuilder;
import com.github.hairless.plink.common.util.FileUtil;
import com.github.hairless.plink.common.util.UploadUtil;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.exception.PlinkMessageException;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.hairless.plink.common.util.MessageFormatUtil.format;

/**
 * @author: silence
 * @date: 2020/1/19
 */
@Slf4j
public class FlinkShellSubmitAssist {
    private final ShellCommandBuilder shellCommandBuilder;
    private final String appIdRegex;
    private final Pattern compile;

    public FlinkShellSubmitAssist(ShellCommandBuilder shellCommandBuilder, String appIdRegex) {
        this.shellCommandBuilder = shellCommandBuilder;
        this.appIdRegex = appIdRegex;
        compile = Pattern.compile(appIdRegex);
    }

    public String submitJob(JobInstanceDTO jobInstanceDTO, String logFile) throws Exception {
        FlinkSubmitOptions flinkSubmitOptions = new FlinkSubmitOptions();
        flinkSubmitOptions.setJobName(jobInstanceDTO.getJob().getName());
        flinkSubmitOptions.setMainJarPath(UploadUtil.getJobJarsPath(jobInstanceDTO.getJobId(), jobInstanceDTO.getConfig().getJarName()));
        flinkSubmitOptions.setFlinkConfig(jobInstanceDTO.getConfig());
        String runCommand = shellCommandBuilder.buildRunCommand(flinkSubmitOptions);
        log.debug("runCommand:{}", runCommand);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/sh", "-c", format("{0} >> {1} 2>&1", runCommand, logFile));
        log.info("--------------------------------submit client log begin----------------------------------");
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        log.info("--------------------------------submit client log end----------------------------------");
        if (exitCode < 0) {
            throw new PlinkMessageException("submit job failed!");
        }
        String log = FileUtil.readFileToString(logFile);
        Matcher matcher = compile.matcher(log);
        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }
        return null;
    }
}
