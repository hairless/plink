package com.github.hairless.plink.common.builder;

import com.github.hairless.plink.common.util.FlinkConfigUtil;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.exception.PlinkException;
import org.apache.flink.util.Preconditions;

import java.util.stream.Collectors;

import static com.github.hairless.plink.common.util.MessageFormatUtil.format;

/**
 * @author: silence
 * @date: 2020/8/24
 */
public class StandaloneCommandBuilder implements ShellCommandBuilder {

    public static final StandaloneCommandBuilder INSTANCE = new StandaloneCommandBuilder();

    private StandaloneCommandBuilder() {
    }

    private static final String runScript = "{0}/bin/flink run ";
    private static final String detached = "-d ";
    private static final String parallelism = "-p {0} ";
    private static final String confItem = "-D {0} ";
    private static final String mainClass = "-c {0} ";
    private static final String mainJarPath = "{0} ";
    private static final String args = "{0}";

    @Override
    public String buildRunCommand(FlinkSubmitOptions flinkSubmitOptions) throws PlinkException {
        FlinkConfig flinkConfig = flinkSubmitOptions.getFlinkConfig();
        StringBuilder builder = new StringBuilder();
        builder.append(format(runScript, FlinkConfigUtil.getFlinkHome())).append(detached);
        if (flinkConfig.getParallelism() != null) {
            builder.append(format(parallelism, flinkConfig.getParallelism()));
        }
        if (flinkConfig.getConfigs() != null) {
            builder.append(flinkConfig.getConfigs().stream().map(c -> format(confItem, c)).collect(Collectors.joining()));
        }
        if (flinkConfig.getMainClass() != null) {
            builder.append(format(mainClass, flinkConfig.getMainClass()));
        }
        builder.append(format(mainJarPath, Preconditions.checkNotNull(flinkSubmitOptions.getMainJarPath())));
        if (flinkConfig.getArgs() != null) {
            builder.append(format(args, flinkConfig.getArgs()));
        }
        return builder.toString();
    }
}
