package com.github.hairless.plink.sql.model.sqlparse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: silence
 * @date: 2020/7/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlParseLink {
    private String sourceName;
    private String targetName;
}