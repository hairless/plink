package com.github.hairless.plink.sql.util;

import com.github.hairless.plink.sql.model.sqlparse.*;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.flink.sql.parser.ddl.SqlCreateTable;
import org.apache.flink.sql.parser.ddl.SqlCreateView;
import org.apache.flink.sql.parser.ddl.SqlTableColumn;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;
import org.apache.flink.sql.parser.validate.FlinkSqlConformance;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentImpl;
import org.apache.flink.table.operations.CatalogSinkModifyOperation;
import org.apache.flink.table.operations.ddl.CreateViewOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: silence
 * @date: 2020/7/20
 */
public class SqlParserUtil {

    public static SqlParser.Config sqlParserConfig = SqlParser
            .configBuilder()
            .setParserFactory(FlinkSqlParserImpl.FACTORY)
            .setConformance(FlinkSqlConformance.DEFAULT)
            .setLex(Lex.JAVA)
            .setIdentifierMaxLength(256)
            .build();

    public static SqlParseInfo parse(String sql) throws SqlParseException {
        StreamExecutionEnvironment env = new LocalStreamEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        TableEnvironmentImpl tEnv = (TableEnvironmentImpl) StreamTableEnvironment.create(env, settings);

        SqlParseInfo sqlParseInfo = new SqlParseInfo();
        SqlParser sqlParser = SqlParser.create(sql, sqlParserConfig);
        SqlNodeList sqlNodes = sqlParser.parseStmtList();
        List<SqlParseNode> nodeList = new ArrayList<>();
        List<SqlParseLink> linkList = new ArrayList<>();
        int insertNodeNum = 0;
        for (SqlNode sqlNode : sqlNodes) {
            String splitSql = sqlNode.toSqlString(SkipAnsiCheckSqlDialect.DEFAULT).getSql();
            tEnv.sqlUpdate(splitSql);

            if (sqlNode instanceof SqlCreateTable) {
                SqlCreateTable sqlCreateTable = (SqlCreateTable) sqlNode;
                SqlParseNode node = new SqlParseNode();
                node.setSql(splitSql);
                node.setName(sqlCreateTable.getTableName().getSimple());
                node.setType(SqlParseNodeTypeEnum.TABLE);
                List<SqlParseColumn> sqlParseColumnList = sqlCreateTable.getColumnList().getList().stream().map(c -> {
                    SqlTableColumn sqlTableColumn = (SqlTableColumn) c;
                    SqlParseColumn sqlParseColumn = new SqlParseColumn();
                    sqlParseColumn.setName(sqlTableColumn.getName().getSimple());
                    sqlParseColumn.setType(sqlTableColumn.getType().toString());
                    if (sqlTableColumn.getComment().isPresent()) {
                        sqlParseColumn.setDesc(sqlTableColumn.getComment().get().getStringValue());
                    }
                    return sqlParseColumn;
                }).collect(Collectors.toList());
                node.setSqlParseColumnList(sqlParseColumnList);
                nodeList.add(node);
            } else if (sqlNode instanceof SqlCreateView) {
                SqlCreateView sqlCreateView = (SqlCreateView) sqlNode;
                String viewName = sqlCreateView.getViewName().getSimple();
                CreateViewOperation createViewOperation = (CreateViewOperation) tEnv.getParser().parse(splitSql).get(0);
                SqlParseNode node = new SqlParseNode();
                node.setSql(splitSql);
                node.setName(viewName);
                node.setType(SqlParseNodeTypeEnum.VIEW);
                List<SqlParseColumn> sqlParseColumnList = createViewOperation.getCatalogView().getSchema().getTableColumns().stream().map(tableColumn -> {
                    SqlParseColumn sqlParseColumn = new SqlParseColumn();
                    sqlParseColumn.setName(tableColumn.getName());
                    sqlParseColumn.setType(tableColumn.getType().getLogicalType().asSummaryString());
                    return sqlParseColumn;
                }).collect(Collectors.toList());
                node.setSqlParseColumnList(sqlParseColumnList);
                nodeList.add(node);
                List<String> selectTableList = lookupSelectTable(sqlCreateView.getQuery());
                List<SqlParseLink> viewLinkList = selectTable2Link(selectTableList, viewName);
                linkList.addAll(viewLinkList);
            } else if (sqlNode instanceof SqlInsert) {
                SqlInsert sqlInsert = (SqlInsert) sqlNode;
                String sinkTableName = sqlInsert.getTargetTable().toString();
                String insertName = "insert_node_" + insertNodeNum++;
                SqlParseNode node = new SqlParseNode();
                node.setSql(splitSql);
                node.setType(SqlParseNodeTypeEnum.INSERT);
                node.setName(insertName);
                CatalogSinkModifyOperation catalogSinkModifyOperation = (CatalogSinkModifyOperation) tEnv.getParser().parse(splitSql).get(0);
                List<SqlParseColumn> sqlParseColumnList = catalogSinkModifyOperation.getChild().getTableSchema().getTableColumns().stream().map(tableColumn -> {
                    SqlParseColumn sqlParseColumn = new SqlParseColumn();
                    sqlParseColumn.setName(tableColumn.getName());
                    sqlParseColumn.setType(tableColumn.getType().getLogicalType().asSummaryString());
                    return sqlParseColumn;
                }).collect(Collectors.toList());
                node.setSqlParseColumnList(sqlParseColumnList);
                nodeList.add(node);
                List<String> selectTableList = lookupSelectTable(sqlInsert.getSource());
                List<SqlParseLink> sinkLinkList = selectTable2Link(selectTableList, insertName);
                linkList.addAll(sinkLinkList);
                linkList.add(new SqlParseLink(insertName, sinkTableName));
            }
        }
        sqlParseInfo.setNodeList(nodeList);
        sqlParseInfo.setLinkList(linkList);
        //校验
        tEnv.explain(true);
        return sqlParseInfo;
    }

    private static List<SqlParseLink> selectTable2Link(List<String> selectTableList, String target) {
        return selectTableList.stream().map(selectTable -> {
            SqlParseLink link = new SqlParseLink();
            link.setSource(selectTable);
            link.setTarget(target);
            return link;
        }).collect(Collectors.toList());
    }

    private static List<String> lookupSelectTable(SqlNode sqlNode) {
        List<String> list = new ArrayList<>();
        if (sqlNode instanceof SqlSelect) {
            SqlNode from = ((SqlSelect) sqlNode).getFrom();
            list.addAll(lookupSelectTable(from));
        } else if (sqlNode instanceof SqlJoin) {
            SqlJoin sqlJoin = (SqlJoin) sqlNode;
            list.addAll(lookupSelectTable(sqlJoin.getLeft()));
            list.addAll(lookupSelectTable(sqlJoin.getRight()));
        } else if (sqlNode instanceof SqlBasicCall) {
            SqlBasicCall sqlBasicCall = (SqlBasicCall) sqlNode;
            SqlOperator operator = sqlBasicCall.getOperator();
            if (SqlKind.AS.equals(operator.getKind())) {
                list.addAll(lookupSelectTable(sqlBasicCall.getOperands()[0]));
            } else if (SqlKind.UNION.equals(operator.getKind())) {
                for (SqlNode operandSqlNode : sqlBasicCall.getOperands()) {
                    list.addAll(lookupSelectTable(operandSqlNode));
                }
            } else {
                throw new RuntimeException("operator " + operator.getKind() + " not support");
            }
        } else if (sqlNode instanceof SqlIdentifier) {
            list.add(((SqlIdentifier) sqlNode).getSimple());
        } else {
            throw new RuntimeException("operator " + sqlNode.getClass() + " not support");
        }
        return list;
    }
}
