package com.github.hairless.plink.common.conf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.hairless.plink.common.util.FileUtil;
import com.github.hairless.plink.common.util.JsonUtil;
import com.github.hairless.plink.model.common.UIOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/27
 */
@Slf4j
@ConfigurationProperties(prefix = "flink")
public class FlinkAutoConfig {
    public static Map<String, String> defaultConfs;
    private static final String FLINK_SUBMIT_OPTION_CONFIG_PATH = "file:./config/flinkSubmitOptionConfig.json";
    public static List<UIOption> uiOptions;

    static {
        try {
            Resource resource = new PathMatchingResourcePatternResolver().getResource(FLINK_SUBMIT_OPTION_CONFIG_PATH);
            String configJson = FileUtil.readFileToString(resource.getFile());
            uiOptions = JsonUtil.parseObject(configJson, new TypeReference<List<UIOption>>() {
            });
        } catch (IOException e) {
            log.error("load flinkSubmitOptionConfig error!", e);
        }
    }

    void setDefaultConfs(Map<String, String> defaultConfs) {
        FlinkAutoConfig.defaultConfs = defaultConfs;
    }
}

