package com.github.hairless.plink.common.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: silence
 * @date: 2021/02/20
 */
@ConfigurationProperties(prefix = "cluster")
public class ClusterAutoConfig {
    public static String mode;
    public static String queue;

    void setMode(String mode) {
        ClusterAutoConfig.mode = mode;
    }

    void setQueue(String queue) {
        ClusterAutoConfig.queue = queue;
    }
}
