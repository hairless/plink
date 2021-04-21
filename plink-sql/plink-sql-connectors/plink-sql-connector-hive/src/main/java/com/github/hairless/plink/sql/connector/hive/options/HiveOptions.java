package com.github.hairless.plink.sql.connector.hive.options;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

/**
 * @author chaixiaoxue
 * @version 1.0
 * @date 2021/3/19 10:47
 */
public class HiveOptions {
    public static final ConfigOption<String> HIVE_CONF_DIR =
        ConfigOptions.key("hive.conf.dir")
            .stringType()
            .noDefaultValue()
            .withDescription("Required hive conf dir");

    public static final ConfigOption<String> HIVE_VERSION =
        ConfigOptions.key("hive.version")
            .stringType()
            .noDefaultValue()
            .withDescription("Required hive version");

    public static final ConfigOption<String> HIVE_DATABASE =
        ConfigOptions.key("hive.database")
            .stringType()
            .noDefaultValue()
            .withDescription("Required hive database");


    public static final ConfigOption<String> HIVE_TABLE =
        ConfigOptions.key("hive.table")
            .stringType()
            .noDefaultValue()
            .withDescription("Required hive table");
}
