package com.github.hairless.plink.sql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: silence
 * @date: 2020/7/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlConfig {
    private String jobName;
    private String sql;
}
