package com.github.hairless.plink.metrics.checkpoint;

import org.apache.flink.annotation.docs.Documentation;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

@Documentation.SuffixOption
public class PlinkCheckpointReporterOptions {

    public static final ConfigOption<String> MODE =
            ConfigOptions.key("mode")
                    .defaultValue("standalone")
                    .withDescription("The Flink cluster mode.");

    public static final ConfigOption<String> SERVICE =
            ConfigOptions.key("service")
                    .noDefaultValue()
                    .withDescription("The Reporter remote service.");

    public static final ConfigOption<String> PLINK_JOB_ID =
            ConfigOptions.key("jobId")
                    .noDefaultValue()
                    .withDescription("The Plink job id");

    public static final ConfigOption<String> PLINK_INSTANCE_ID =
            ConfigOptions.key("instanceId")
                    .noDefaultValue()
                    .withDescription(
                            "The Plink job instance id.");

}
