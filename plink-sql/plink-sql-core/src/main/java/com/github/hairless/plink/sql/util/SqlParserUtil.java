package com.github.hairless.plink.sql.util;

import com.github.hairless.plink.sql.model.Column;
import com.github.hairless.plink.sql.model.SqlParseResult;
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

    public static SqlParseResult parse(String sql) throws SqlParseException {
        StreamExecutionEnvironment env = new LocalStreamEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        TableEnvironmentImpl tEnv = (TableEnvironmentImpl) StreamTableEnvironment.create(env, settings);

        SqlParseResult sqlParseResult = new SqlParseResult();
        SqlParser sqlParser = SqlParser.create(sql, sqlParserConfig);
        SqlNodeList sqlNodes = sqlParser.parseStmtList();
        List<SqlParseResult.Node> nodeList = new ArrayList<>();
        List<SqlParseResult.Link> linkList = new ArrayList<>();
        sqlNodes.forEach(sqlNode -> {
            String splitSql = sqlNode.toSqlString(SkipAnsiCheckSqlDialect.DEFAULT).getSql();
            tEnv.sqlUpdate(splitSql);

            if (sqlNode instanceof SqlCreateTable) {
                SqlCreateTable sqlCreateTable = (SqlCreateTable) sqlNode;
                SqlParseResult.Node node = new SqlParseResult.Node();
                node.setSql(splitSql);
                node.setName(sqlCreateTable.getTableName().getSimple());
                node.setType("table");
                List<Column> columnList = sqlCreateTable.getColumnList().getList().stream().map(c -> {
                    SqlTableColumn sqlTableColumn = (SqlTableColumn) c;
                    Column column = new Column();
                    column.setName(sqlTableColumn.getName().getSimple());
                    column.setType(sqlTableColumn.getType().toString());
                    if (sqlTableColumn.getComment().isPresent()) {
                        column.setDesc(sqlTableColumn.getComment().get().getStringValue());
                    }
                    return column;
                }).collect(Collectors.toList());
                node.setColumnList(columnList);
                nodeList.add(node);
            } else if (sqlNode instanceof SqlCreateView) {
                SqlCreateView sqlCreateView = (SqlCreateView) sqlNode;
                String viewName = sqlCreateView.getViewName().getSimple();
                CreateViewOperation createViewOperation = (CreateViewOperation) tEnv.getParser().parse(splitSql).get(0);
                SqlParseResult.Node node = new SqlParseResult.Node();
                node.setSql(splitSql);
                node.setName(viewName);
                node.setType("view");
                List<Column> columnList = createViewOperation.getCatalogView().getSchema().getTableColumns().stream().map(tableColumn -> {
                    Column column = new Column();
                    column.setName(tableColumn.getName());
                    column.setType(tableColumn.getType().getLogicalType().asSummaryString());
                    return column;
                }).collect(Collectors.toList());
                node.setColumnList(columnList);
                nodeList.add(node);
                List<String> selectTableList = lookupSelectTable(sqlCreateView.getQuery());
                List<SqlParseResult.Link> viewLinkList = selectTable2Link(selectTableList, viewName);
                linkList.addAll(viewLinkList);
            } else if (sqlNode instanceof SqlInsert) {
                SqlInsert sqlInsert = (SqlInsert) sqlNode;
                String targetTableName = sqlInsert.getTargetTable().toString();
                List<String> selectTableList = lookupSelectTable(sqlInsert.getSource());
                List<SqlParseResult.Link> sinkLinkList = selectTable2Link(selectTableList, targetTableName);
                linkList.addAll(sinkLinkList);
            }
        });
        sqlParseResult.setNodeList(nodeList);
        sqlParseResult.setLinkList(linkList);
        //校验
        tEnv.explain(true);
        return sqlParseResult;
    }

    private static List<SqlParseResult.Link> selectTable2Link(List<String> selectTableList, String target) {
        return selectTableList.stream().map(selectTable -> {
            SqlParseResult.Link link = new SqlParseResult.Link();
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
