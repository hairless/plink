package com.github.hairless.plink.sql.model;

import lombok.AllArgsConstructor;
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceConfig {
        private List<String> data;
    }
}
