package com.github.hairless.plink.sql.model.sqlparse;

import lombok.Data;
import org.apache.calcite.sql.SqlNode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: silence
 * @date: 2020/7/29
 */
@Data
public class SqlParseNode {
    private String name;
    private SqlParseNodeTypeEnum type;
    private List<SqlParseColumn> columnList;
    private String comment;
    private String sql;
    private Map<String, String> properties;
    private Set<SqlParseNodeActionEnum> actions = new HashSet<>();

    private transient SqlNode calciteSqlNode;
}