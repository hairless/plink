package com.github.hairless.plink.common.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/27
 */
@ConfigurationProperties(prefix = "flink")
public class FlinkAutoConfig {
    public static Map<String, String> defaultConfs;

    void setDefaultConfs(Map<String, String> defaultConfs) {
        FlinkAutoConfig.defaultConfs = defaultConfs;
    }
}
