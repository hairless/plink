package com.github.hairless.plink.sql;

import com.github.hairless.plink.sql.connector.collection.CollectionDataWarehouse;
import com.github.hairless.plink.sql.connector.collection.CollectionTableFactory;
import com.github.hairless.plink.sql.connector.collection.CollectionTableSink;
import com.github.hairless.plink.sql.model.SqlConfig;
import com.github.hairless.plink.sql.model.SqlDebugConfig;
import com.github.hairless.plink.sql.model.exception.PlinkSqlParseException;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseNode;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseNodeActionEnum;
import com.github.hairless.plink.sql.util.JsonUtil;
import com.github.hairless.plink.sql.util.PlinkSqlParser;
import com.github.hairless.plink.sql.util.SkipAnsiCheckSqlDialect;
import com.github.hairless.plink.sql.util.SqlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.sql.parser.ddl.SqlCreateTable;
import org.apache.flink.sql.parser.ddl.SqlCreateView;
import org.apache.flink.sql.parser.ddl.SqlTableOption;
import org.apache.flink.table.factories.DataGenTableSourceFactory;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.util.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: silence
 * @date: 2020/7/30
 */
@Slf4j
public class SqlDebugDriver {

    public static Map<String, List<String>> debug(SqlDebugConfig sqlDebugConfig) throws Exception {
        Preconditions.checkNotNull(sqlDebugConfig, "sqlDebugConfig should be not null");
        Preconditions.checkNotNull(sqlDebugConfig.getSql(), "sql should be not null");
        String identifier = UUID.randomUUID().toString();
        String debugSql = handleDebugSql(identifier, sqlDebugConfig.getSql(), sqlDebugConfig);
        log.debug("start sql debug,sql:{}", debugSql);
        SqlConfig config = SqlConfig.builder().sql(debugSql).jobName("sql_job_debug_test").build();
        SqlJob sqlJob = new SqlJob(config);
        try {
            CollectionDataWarehouse.registerLock(identifier);
            sqlJob.start();
            return CollectionDataWarehouse.getData(identifier);
        } finally {
            CollectionDataWarehouse.remove(identifier);
        }
    }

    private static String handleDebugSql(final String identifier, String sql, SqlDebugConfig sqlDebugConfig) throws PlinkSqlParseException {
        PlinkSqlParser plinkSqlParser = PlinkSqlParser.create(sql);
        StringBuilder sqlBuilder = new StringBuilder();
        List<SqlParseNode> sourceTableList = plinkSqlParser.getTableList(SqlParseNodeActionEnum.SOURCE);
        sourceTableList.forEach(sourceTable -> {
            SqlDebugConfig.SourceConfig sourceConfig = sqlDebugConfig.getSourceConfigMap().get(sourceTable.getName());
            sqlBuilder.append(buildDebugSourceSql(sourceTable, sourceConfig));
            String sourceTableOutName = sourceTable.getName() + CollectionTableSink.OUT_SUFFIX;
            sqlBuilder.append(buildDebugSinkSql(identifier, sourceTable, sourceTableOutName));
            sqlBuilder.append(buildDebugInsertSql(sourceTable, sourceTableOutName));
        });
        List<SqlParseNode> sinkTableList = plinkSqlParser.getTableList(SqlParseNodeActionEnum.SINK);
        sinkTableList.forEach(sinkTable -> {
            sqlBuilder.append(buildDebugSinkSql(identifier, sinkTable));
        });

        List<SqlParseNode> viewList = plinkSqlParser.getViewList();
        viewList.forEach(view -> {
            sqlBuilder.append(view.getSql()).append(";");
            String viewOutName = view.getName() + CollectionTableSink.OUT_SUFFIX;
            sqlBuilder.append(buildDebugSinkSql(identifier, view, viewOutName));
            sqlBuilder.append(buildDebugInsertSql(view, viewOutName));
        });

        List<SqlParseNode> insertList = plinkSqlParser.getInsertList();
        insertList.forEach(insert -> {
            sqlBuilder.append(insert.getSql()).append(";");
            String insertOutName = insert.getName() + CollectionTableSink.OUT_SUFFIX;
            sqlBuilder.append(buildDebugSinkSql(identifier, insert, insertOutName));
            sqlBuilder.append(buildDebugInsertSql(insert, insertOutName));
        });
        return sqlBuilder.toString();
    }

    private static String buildDebugSourceSql(SqlParseNode sourceTable, SqlDebugConfig.SourceConfig sourceConfig) {
        List<SqlNode> tableOptions = ((SqlCreateTable) sourceTable.getCalciteSqlNode()).getPropertyList().getList();
        List<SqlNode> newTableOptions = new ArrayList<>();
        if (sourceConfig.getDatagen()) {
            newTableOptions.add(newSqlTableOption(FactoryUtil.CONNECTOR.key(), DataGenTableSourceFactory.IDENTIFIER));
            Integer limit = sourceConfig.getLimit();
            assert limit != null;
            //DataGenTableSourceFactory.NUMBER_OF_ROWS.key()
            newTableOptions.add(newSqlTableOption("number-of-rows", limit.toString()));
        } else {
            newTableOptions.addAll(tableOptions.stream().filter(node -> ((SqlTableOption) node).getKeyString().startsWith(FactoryUtil.FORMAT.key())).collect(Collectors.toList()));
            newTableOptions.add(newSqlTableOption(FactoryUtil.CONNECTOR.key(), CollectionTableFactory.COLLECTION));
            newTableOptions.add(newSqlTableOption(CollectionTableFactory.DATA.key(), JsonUtil.toJSONString(sourceConfig.getData())));
        }
        tableOptions.clear();
        tableOptions.addAll(newTableOptions);
        return ((SqlNode) sourceTable.getCalciteSqlNode()).toSqlString(SkipAnsiCheckSqlDialect.DEFAULT).getSql() + ";";
    }

    private static String buildDebugSinkSql(String identifier, SqlParseNode sinkTable) {
        return buildDebugSinkSql(identifier, sinkTable, null);
    }

    private static String buildDebugSinkSql(String identifier, SqlParseNode sinkTable, String newTableName) {
        Map<String, String> properties = sinkTable.getProperties();
        if (properties == null) {
            properties = new HashMap<>();
        }
        Map<String, String> debugProperties = new HashMap<>();
        debugProperties.put(FactoryUtil.CONNECTOR.key(), CollectionTableFactory.COLLECTION);
        debugProperties.put(CollectionTableFactory.IDENTIFIER.key(), identifier);
        Map<String, String> formatProperties = filterFormatProperties(properties);
        debugProperties.putAll(formatProperties);
        return SqlBuilder.tableBuilder()
                .tableName(StringUtils.isEmpty(newTableName) ? sinkTable.getName() : newTableName)
                .columnList(sinkTable.getColumnList())
                .properties(debugProperties)
                .build();
    }

    private static String buildDebugInsertSql(SqlParseNode fromTable, String targetTableName) {
        SqlNode calciteSqlNode = (SqlNode) fromTable.getCalciteSqlNode();
        String query;
        if (calciteSqlNode instanceof SqlCreateView) {
            query = ((SqlCreateView) calciteSqlNode).getQuery().toSqlString(SkipAnsiCheckSqlDialect.DEFAULT).getSql();
        } else if (calciteSqlNode instanceof SqlInsert) {
            query = ((SqlInsert) calciteSqlNode).getSource().toSqlString(SkipAnsiCheckSqlDialect.DEFAULT).getSql();
        } else if (calciteSqlNode instanceof SqlCreateTable) {
            query = "select * from " + fromTable.getName();
        } else {
            throw new RuntimeException(calciteSqlNode.getClass().getSimpleName() + " not support");
        }
        return SqlBuilder.insertBuilder().query(query).targetTableName(targetTableName).columnList(fromTable.getColumnList()).build();
    }


    private static Map<String, String> filterFormatProperties(Map<String, String> properties) {
        return properties.entrySet().stream().filter(entry -> entry.getKey().startsWith(FactoryUtil.FORMAT.key()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static SqlTableOption newSqlTableOption(String key, String value) {
        return new SqlTableOption(SqlLiteral.createCharString(key, SqlParserPos.ZERO), SqlLiteral.createCharString(value, SqlParserPos.ZERO), SqlParserPos.ZERO);
    }
}
