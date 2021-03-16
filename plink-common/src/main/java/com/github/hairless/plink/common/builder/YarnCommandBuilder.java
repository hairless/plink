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
    private static final String mode = "-t yarn-per-job ";
    private static final String detached = "-d ";
    private static final String classpathItem = "-C \"{0}\" ";
    private static final String jobName = "-D \"yarn.application.name={0}\" ";
    private static final String queue = "-D \"yarn.application.queue={0}\" ";
    private static final String yarnShip = "-D \"yarn.ship-files={0}\" ";
    private static final String confItem = "-D \"{0}\" ";
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
        if (StringUtils.isNotBlank(flinkConfig.getQueue())) {
            builder.append(format(queue, flinkConfig.getQueue()));
        }
        if (flinkConfig.getConfigs() != null) {
            builder.append(flinkConfig.getConfigs().entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .map(c -> format(confItem, c))
                    .collect(Collectors.joining()));
        }
        if (flinkSubmitOptions.getLocalClasspath() != null) {
            flinkSubmitOptions.getLocalClasspath().stream().map(c -> format(classpathItem, c)).forEach(builder::append);
        }
        if (flinkSubmitOptions.getShapefiles() != null) {
            flinkSubmitOptions.getShapefiles().stream().map(c -> format(yarnShip, c)).forEach(builder::append);
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
