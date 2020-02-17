package com.github.hairless.plink.common;

import com.github.hairless.plink.model.exception.PlinkMessageException;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.configuration.RestOptions;

/**
 * @author: silence
 * @date: 2020/2/17
 */
public class FlinkConfigUtil {

    private static Configuration configuration;

    public static String getFlinkHome() throws PlinkMessageException {
        String flinkHome = System.getenv("FLINK_HOME");
        if (StringUtils.isBlank(flinkHome)) {
            throw new PlinkMessageException("FLINK_HOME is not set!");
        }
        return flinkHome;
    }

    private static synchronized void loadConfiguration() throws PlinkMessageException {
        if (configuration == null) {
            configuration = GlobalConfiguration.loadConfiguration(getFlinkHome() + "/conf");
        }
    }

    public static Configuration getConfiguration() throws PlinkMessageException {
        if (configuration == null) {
            loadConfiguration();
        }
        return configuration;
    }

    public static String getRestAddress() throws PlinkMessageException {
        return "http://" + getConfiguration().get(RestOptions.ADDRESS) + ":" + getConfiguration().get(RestOptions.PORT);
    }

}
