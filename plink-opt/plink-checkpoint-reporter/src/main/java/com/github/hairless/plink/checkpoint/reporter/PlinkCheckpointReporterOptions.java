package com.github.hairless.plink.checkpoint.reporter;

import org.apache.flink.annotation.docs.Documentation;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

@Documentation.SuffixOption
public class PlinkCheckpointReporterOptions {

    public static final ConfigOption<String> SERVICE = ConfigOptions
        .key("service")
        .stringType()
        .noDefaultValue()
        .withDescription("The Reporter remote service url.");

    public static final ConfigOption<String> PLINK_JOB_ID = ConfigOptions
        .key("jobId")
        .stringType()
        .noDefaultValue()
        .withDescription("The Plink job id.");

    public static final ConfigOption<String> PLINK_INSTANCE_ID = ConfigOptions
        .key("instanceId")
        .stringType()
        .noDefaultValue()
        .withDescription("The Plink job instance id.");

}
