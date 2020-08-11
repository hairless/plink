package com.github.hairless.plink.sql.model.sqlparse;

import lombok.Data;

/**
 * @author: silence
 * @date: 2020/7/17
 */
@Data
public class SqlParseColumn {
    private String name;
    private String type;
    private String desc;
}
