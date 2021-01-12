package com.github.hairless.plink.sql.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/7/30
 */
@Data
public class SqlDebugConfig {

    private String sql;
    private Map<String, SourceConfig> sourceConfigMap;

    /**
     * datagen:开启数据自动生成，需结合limit使用
     * <p>
     * data：显示提供样例数据
     */
    @Data
    @NoArgsConstructor
    public static class SourceConfig {
        private Boolean datagen = false;
        private Integer limit;
        private List<String> data;
    }
}
