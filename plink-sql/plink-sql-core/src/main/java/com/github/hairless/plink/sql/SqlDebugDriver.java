package com.github.hairless.plink.sql;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.sql.model.SqlConfig;
import com.github.hairless.plink.sql.model.SqlDebugConfig;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseNode;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseNodeActionEnum;
import com.github.hairless.plink.sql.util.PlinkSqlParser;
import com.github.hairless.plink.sql.util.SqlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.sql.parser.ddl.SqlCreateView;
import org.apache.flink.table.descriptors.FormatDescriptorValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: silence
 * @date: 2020/7/30
 */
@Slf4j
public class SqlDebugDriver {

    public static void debug(String sql, SqlDebugConfig sqlDebugConfig) throws Exception {
        String debugSql = handleDebugSql(sql, sqlDebugConfig);
        log.debug("start sql debug,sql:{}", debugSql);
        SqlConfig config = SqlConfig.builder().sql(debugSql).jobName("sql_job_debug_test").build();
        SqlJob sqlJob = new SqlJob(config);
        sqlJob.start();
    }

    private static String handleDebugSql(String sql, SqlDebugConfig sqlDebugConfig) throws SqlParseException {
        PlinkSqlParser plinkSqlParser = PlinkSqlParser.create(sql);
        StringBuilder sqlBuilder = new StringBuilder();
        List<SqlParseNode> sourceTableList = plinkSqlParser.getTableList(SqlParseNodeActionEnum.SOURCE);
        sourceTableList.forEach(sourceTable -> {
            SqlDebugConfig.SourceConfig sourceConfig = sqlDebugConfig.getMap().get(sourceTable.getName());
            sqlBuilder.append(buildDebugSourceSql(sourceTable, sourceConfig));
        });
        List<SqlParseNode> sinkTableList = plinkSqlParser.getTableList(SqlParseNodeActionEnum.SINK);
        sinkTableList.forEach(sinkTable -> {
            sqlBuilder.append(buildDebugSinkSql(sinkTable));
        });

        List<SqlParseNode> viewList = plinkSqlParser.getViewList();
        viewList.forEach(view -> {
            sqlBuilder.append(view.getSql()).append(";");
            String viewOutName = view.getName() + "_out";
            sqlBuilder.append(buildDebugSinkSql(view, viewOutName));
            sqlBuilder.append(buildDebugInsertSql(view, viewOutName));
        });

        List<SqlParseNode> insertList = plinkSqlParser.getInsertList();
        insertList.forEach(insert -> {
            sqlBuilder.append(insert.getSql()).append(";");
            String insertOutName = insert.getName() + "_out";
            sqlBuilder.append(buildDebugSinkSql(insert, insertOutName));
            sqlBuilder.append(buildDebugInsertSql(insert, insertOutName));
        });
        return sqlBuilder.toString();
    }

    private static String buildDebugSourceSql(SqlParseNode sourceTable, SqlDebugConfig.SourceConfig sourceConfig) {
        Map<String, String> properties = sourceTable.getProperties();
        Map<String, String> debugProperties = new HashMap<>();
        debugProperties.put("connector", "collection");
        debugProperties.put("data", JSON.toJSONString(sourceConfig.getData()));
        debugProperties.putAll(filterFormatProperties(properties));
        return SqlBuilder.tableBuilder().tableName(sourceTable.getName()).columnList(sourceTable.getColumnList()).properties(debugProperties).build();
    }

    private static String buildDebugSinkSql(SqlParseNode sinkTable) {
        return buildDebugSinkSql(sinkTable, null);
    }

    private static String buildDebugSinkSql(SqlParseNode sinkTable, String newTableName) {
        Map<String, String> properties = sinkTable.getProperties();
        if (properties == null) {
            properties = new HashMap<>();
            properties.put(FormatDescriptorValidator.FORMAT_TYPE, "json");
        }
        Map<String, String> debugProperties = new HashMap<>();
        debugProperties.put("connector", "collection");
        debugProperties.putAll(filterFormatProperties(properties));
        return SqlBuilder.tableBuilder()
                .tableName(StringUtils.isEmpty(newTableName) ? sinkTable.getName() : newTableName)
                .columnList(sinkTable.getColumnList())
                .properties(debugProperties)
                .build();
    }

    private static String buildDebugInsertSql(SqlParseNode fromTable, String targetTableName) {
        SqlNode calciteSqlNode = fromTable.getCalciteSqlNode();
        String query;
        if (calciteSqlNode instanceof SqlCreateView) {
            query = ((SqlCreateView) calciteSqlNode).getQuery().toString();
        } else if (calciteSqlNode instanceof SqlInsert) {
            query = ((SqlInsert) calciteSqlNode).getSource().toString();
        } else {
            throw new RuntimeException("xxx");
        }
        return SqlBuilder.insertBuilder().query(query).targetTableName(targetTableName).columnList(fromTable.getColumnList()).build();
    }


    private static Map<String, String> filterFormatProperties(Map<String, String> properties) {
        return properties.entrySet().stream().filter(entry -> entry.getKey().startsWith(FormatDescriptorValidator.FORMAT))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
