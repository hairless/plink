package com.github.hairless.plink.service.tools;

import lombok.Getter;

/**
 * @author aven danxieai@163.com
 * @version 0.1
 * @date 2020/1/24
 */
public enum FlinkPropertiesEnum {
    REST_PORT("rest.port", "8081"),
    REST_HOST("rest.host", "localhost");

    @Getter
    private String key;
    @Getter
    private String defaultValue;

    FlinkPropertiesEnum(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }
}
