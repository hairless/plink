package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.builder.JobBuilder;
import com.github.hairless.plink.common.factory.JobBuilderFactory;
import com.github.hairless.plink.common.util.FileUtil;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.enums.JobTypeEnum;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.service.FlinkClusterService;
import com.github.hairless.plink.service.JobInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.dag.Pipeline;
import org.apache.flink.client.deployment.executors.LocalExecutor;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.core.execution.JobClient;
import org.apache.flink.runtime.client.JobCancellationException;
import org.apache.flink.runtime.client.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: silence
 * @date: 2021/1/19
 */
@Slf4j
@Service("localFlinkClusterServiceImpl")
public class LocalFlinkClusterServiceImpl implements FlinkClusterService {
    private static final ConcurrentHashMap<Long, JobClient> jobClientMap = new ConcurrentHashMap<>();

    @Autowired
    private JobInstanceService jobInstanceService;

    private static final Pattern compile = Pattern.compile("Web frontend listening at (http[a-zA-Z0-9:\\/_\\.]+).");

    @Override
    public String submitJob(JobInstanceDTO jobInstanceDTO) throws Exception {
        JobTypeEnum jobTypeEnum = JobTypeEnum.getEnum(jobInstanceDTO.getJob().getType());
        JobBuilder jobBuilder = JobBuilderFactory.create(jobTypeEnum);
        FlinkSubmitOptions flinkSubmitOptions = jobBuilder.buildFlinkSubmitOption(jobInstanceDTO);
        Configuration configuration = Configuration.fromMap(flinkSubmitOptions.getFlinkConfig().getConfigs());
        configuration.setBoolean(DeploymentOptions.ATTACHED, true);
        PackagedProgram packagedProgram = PackagedProgram.newBuilder()
                .setConfiguration(configuration)
                .setEntryPointClassName(flinkSubmitOptions.getFlinkConfig().getMainClass())
                .setArguments(flinkSubmitOptions.getFlinkConfig().getPrepArgs())
                .setJarFile(new File(flinkSubmitOptions.getMainJarPath()))
                .setUserClassPaths(flinkSubmitOptions.getLocalClasspath())
                .build();
        Pipeline pipelineFromProgram = PackagedProgramUtils.getPipelineFromProgram(packagedProgram, configuration, 1, false);
        CompletableFuture<JobClient> execute = LocalExecutor.create(configuration).execute(pipelineFromProgram, configuration, packagedProgram.getUserCodeClassLoader());
        JobClient jobClient = execute.get();
        jobClient.getJobExecutionResult().handleAsync((res, e) -> {
            JobInstance jobInstanceStopped = new JobInstance();
            if (e instanceof CompletionException) {
                if (e.getCause() instanceof JobCancellationException) {
                    jobInstanceStopped.setStatus(JobInstanceStatusEnum.STOPPED.getValue());
                } else if (e.getCause() instanceof JobExecutionException) {
                    jobInstanceStopped.setStatus(JobInstanceStatusEnum.RUN_FAILED.getValue());
                } else {
                    jobInstanceStopped.setStatus(JobInstanceStatusEnum.SUCCESS.getValue());
                }
            } else {
                jobInstanceStopped.setStatus(JobInstanceStatusEnum.UNKNOWN.getValue());
            }
            jobInstanceStopped.setStopTime(new Date());
            jobInstanceStopped.setId(jobInstanceDTO.getId());
            jobInstanceStopped.setJobId(jobInstanceDTO.getJobId());
            jobInstanceService.updateJobAndInstanceStatus(jobInstanceStopped);
            return res;
        });
        jobClientMap.put(jobInstanceDTO.getId(), jobClient);
        return jobClient.getJobID().toString();
    }

    @Override
    public JobInstanceStatusEnum jobStatus(JobInstanceDTO jobInstanceDTO) throws Exception {
        return JobInstanceStatusEnum.RUNNING;
    }

    @Override
    public void stopJob(JobInstanceDTO jobInstanceDTO) throws Exception {
        JobClient jobClient = jobClientMap.get(jobInstanceDTO.getId());
        if (jobClient != null) {
            jobClient.cancel().join();
        }
    }

    @Override
    public String getJobUiAddress(JobInstanceDTO jobInstanceDTO) throws Exception {
        String logFile = jobInstanceService.getClientLogFilePath(jobInstanceDTO);
        if (FileUtil.exists(logFile)) {
            String log = FileUtil.readFileToString(logFile);
            Matcher matcher = compile.matcher(log);
            if (matcher.find() && matcher.groupCount() == 1) {
                return matcher.group(1);
            }
        }
        return null;
    }
}
