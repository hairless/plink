package com.github.hairless.plink.checkpoint.container;

import com.github.hairless.plink.checkpoint.agent.AgentLocation;
import lombok.Getter;
import lombok.Setter;
import org.apache.flink.configuration.Configuration;

/**
 * @author lipan
 * @date 2021-01-24
 */
public class ConfigurationHolder {
    private static Configuration config;
    @Setter
    @Getter
    private static AgentLocation location;

    public static void init(Configuration configuration) {
        config = configuration;
    }

    public static Configuration config() {
        return config;
    }
}
