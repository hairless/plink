package com.github.hairless.plink.sql.model.sqlparse;

import lombok.Data;

import java.util.List;

/**
 * @author: silence
 * @date: 2020/7/29
 */
@Data
public class SqlParseNode {
    private String name;
    private SqlParseNodeTypeEnum type;
    private List<SqlParseColumn> sqlParseColumnList;
    private String sql;
}