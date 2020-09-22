package com.github.hairless.plink.sql.util;

import com.github.hairless.plink.sql.model.sqlparse.SqlParseColumn;
import org.apache.flink.util.Preconditions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: silence
 * @date: 2020/7/30
 */
public class SqlBuilder {


    private SqlBuilder() {
    }

    public static TableSqlBuilder tableBuilder() {
        return new TableSqlBuilder();
    }

    public static InsertSqlBuilder insertBuilder() {
        return new InsertSqlBuilder();
    }

    public static class TableSqlBuilder {
        private String tableName;
        private List<SqlParseColumn> columnList;
        private Map<String, String> properties;

        private TableSqlBuilder() {
        }

        public TableSqlBuilder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public TableSqlBuilder columnList(List<SqlParseColumn> columnList) {
            this.columnList = columnList;
            return this;
        }

        public TableSqlBuilder properties(Map<String, String> properties) {
            this.properties = properties;
            return this;
        }

        public String build() {
            this.checkParams();
            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE `").append(tableName).append("`(");
            String columnString = columnList.stream()
                    .peek(column -> column.setType(column.getType().replace("*ROWTIME*", "")))
                    .map(column -> String.format("`%s` %s", column.getName(), column.getType())).collect(Collectors.joining(","));
            sql.append(columnString).append(") WITH(");
            String propertiesString = properties.entrySet().stream().map(prop -> String.format("'%s'='%s'", prop.getKey(), prop.getValue())).collect(Collectors.joining(","));
            sql.append(propertiesString).append(");");
            return sql.toString();
        }

        private void checkParams() {
            Preconditions.checkNotNull(tableName, "tableName must not be null.");
            Preconditions.checkNotNull(columnList, "columnList must not be null.");
            Preconditions.checkNotNull(properties, "properties must not be null.");
        }
    }

    public static class InsertSqlBuilder {
        private String query;
        private String targetTableName;
        private List<SqlParseColumn> columnList;

        private InsertSqlBuilder() {
        }

        public InsertSqlBuilder query(String query) {
            this.query = query;
            return this;
        }

        public InsertSqlBuilder targetTableName(String targetTableName) {
            this.targetTableName = targetTableName;
            return this;
        }

        public InsertSqlBuilder columnList(List<SqlParseColumn> columnList) {
            this.columnList = columnList;
            return this;
        }

        public String build() {
            this.checkParams();
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO `").append(targetTableName).append("`(");
            String columnString = columnList.stream()
                    .map(column -> String.format("`%s`", column.getName())).collect(Collectors.joining(","));
            sql.append(columnString).append(") ").append(query).append(";");
            return sql.toString();
        }

        private void checkParams() {
            Preconditions.checkNotNull(query, "query must not be null.");
            Preconditions.checkNotNull(targetTableName, "targetTableName must not be null.");
            Preconditions.checkNotNull(columnList, "columnList must not be null.");
        }
    }

}
