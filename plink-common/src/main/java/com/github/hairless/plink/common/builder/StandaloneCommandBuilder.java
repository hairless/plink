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
public class StandaloneCommandBuilder implements FlinkShellCommandBuilder {

    public static final StandaloneCommandBuilder INSTANCE = new StandaloneCommandBuilder();

    private StandaloneCommandBuilder() {
    }

    private static final String runScript = "{0}/bin/flink run ";
    private static final String detached = "-d ";
    private static final String confItem = "-D {0} ";
    private static final String mainClass = "-c {0} ";
    private static final String mainJarPath = "{0} ";
    private static final String args = "{0}";

    @Override
    public String buildRunCommand(FlinkSubmitOptions flinkSubmitOptions) throws PlinkException {
        FlinkConfig flinkConfig = flinkSubmitOptions.getFlinkConfig();
        StringBuilder builder = new StringBuilder();
        builder.append(format(runScript, FlinkConfigUtil.getFlinkHome())).append(detached);
        if (flinkConfig.getConfigs() != null) {
            builder.append(flinkConfig.getConfigs().entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .map(c -> format(confItem, c))
                    .collect(Collectors.joining()));
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
