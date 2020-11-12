package com.github.hairless.plink.common.builder;

import com.github.hairless.plink.common.util.FlinkConfigUtil;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.exception.PlinkException;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.util.Preconditions;

import java.util.stream.Collectors;

import static com.github.hairless.plink.common.util.MessageFormatUtil.format;

/**
 * @author: silence
 * @date: 2020/8/24
 */
public class YarnCommandBuilder implements FlinkShellCommandBuilder {

    public static final YarnCommandBuilder INSTANCE = new YarnCommandBuilder();

    private YarnCommandBuilder() {
    }

    private static final String exportHadoopClasspath = "export HADOOP_CLASSPATH=`$HADOOP_HOME/bin/hadoop classpath` && ";
    private static final String runScript = "{0}/bin/flink run ";
    private static final String jobName = "-ynm \"{0}\" ";
    private static final String mode = "-m yarn-cluster ";
    private static final String detached = "-d ";
    private static final String queue = "-yqu {0} ";
    private static final String jobManagerMemory = "-yjm {0} ";
    private static final String taskManagerMemory = "-ytm {0} ";
    private static final String taskManagerSlots = "-ys {0} ";
    private static final String parallelism = "-p {0} ";
    private static final String confItem = "-yD {0} ";
    private static final String yarnShip = "-yt {0} ";
    private static final String mainClass = "-c {0} ";
    private static final String mainJarPath = "{0} ";
    private static final String args = "{0}";

    @Override
    public String buildRunCommand(FlinkSubmitOptions flinkSubmitOptions) throws PlinkException {
        FlinkConfig flinkConfig = flinkSubmitOptions.getFlinkConfig();
        StringBuilder builder = new StringBuilder();
        builder.append(exportHadoopClasspath).append(format(runScript, FlinkConfigUtil.getFlinkHome())).append(mode).append(detached);
        if (StringUtils.isNotBlank(flinkSubmitOptions.getJobName())) {
            builder.append(format(jobName, flinkSubmitOptions.getJobName()));
        }
        if (StringUtils.isNotBlank(flinkSubmitOptions.getQueue())) {
            builder.append(format(queue, flinkSubmitOptions.getQueue()));
        }
        if (StringUtils.isNotBlank(flinkConfig.getJobManagerMemory())) {
            builder.append(format(jobManagerMemory, flinkConfig.getJobManagerMemory()));
        }
        if (StringUtils.isNotBlank(flinkConfig.getTaskManagerMemory())) {
            builder.append(format(taskManagerMemory, flinkConfig.getTaskManagerMemory()));
        }
        if (flinkConfig.getTaskManagerSlots() != null) {
            builder.append(format(taskManagerSlots, flinkConfig.getTaskManagerSlots()));
        }
        if (flinkConfig.getParallelism() != null) {
            builder.append(format(parallelism, flinkConfig.getParallelism()));
        }
        if (flinkConfig.getConfigs() != null) {
            builder.append(flinkConfig.getConfigs().entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .map(c -> format(confItem, c))
                    .collect(Collectors.joining()));
        }
        if (StringUtils.isNotBlank(flinkSubmitOptions.getLibPath())) {
            builder.append(format(yarnShip, flinkSubmitOptions.getLibPath()));
        }
        if (StringUtils.isNotBlank(flinkConfig.getMainClass())) {
            builder.append(format(mainClass, flinkConfig.getMainClass()));
        }
        builder.append(format(mainJarPath, Preconditions.checkNotNull(flinkSubmitOptions.getMainJarPath())));
        if (StringUtils.isNotBlank(flinkConfig.getArgs())) {
            builder.append(format(args, flinkConfig.getArgs()));
        }
        return builder.toString();
    }
}
