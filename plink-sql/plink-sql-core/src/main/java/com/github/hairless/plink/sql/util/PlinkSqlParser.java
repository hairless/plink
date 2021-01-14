package com.github.hairless.plink.sql.util;

import com.github.hairless.plink.sql.model.exception.PlinkSqlParseException;
import com.github.hairless.plink.sql.model.sqlparse.*;
import org.apache.calcite.config.Lex;
import org.apache.calcite.runtime.CalciteContextException;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.flink.sql.parser.ddl.SqlCreateTable;
import org.apache.flink.sql.parser.ddl.SqlCreateView;
import org.apache.flink.sql.parser.ddl.SqlTableOption;
import org.apache.flink.sql.parser.dml.RichSqlInsert;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;
import org.apache.flink.sql.parser.validate.FlinkSqlConformance;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentImpl;
import org.apache.flink.table.operations.CatalogSinkModifyOperation;
import org.apache.flink.table.operations.ddl.CreateTableOperation;
import org.apache.flink.table.operations.ddl.CreateViewOperation;
import org.apache.flink.util.ExceptionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: silence
 * @date: 2020/7/20
 */
public class PlinkSqlParser {
    private final Map<String, SqlParseNode> nodeMap;
    private final List<SqlParseLink> linkList;

    public static SqlParser.Config sqlParserConfig = SqlParser
            .configBuilder()
            .setParserFactory(FlinkSqlParserImpl.FACTORY)
            .setConformance(FlinkSqlConformance.DEFAULT)
            .setLex(Lex.JAVA)
            .setIdentifierMaxLength(256)
            .build();

    private PlinkSqlParser(Map<String, SqlParseNode> nodeMap, List<SqlParseLink> linkList) {
        this.nodeMap = nodeMap;
        this.linkList = linkList;
    }

    public List<SqlParseNode> getTableList() {
        return nodeMap.values().stream().filter(node -> SqlParseNodeTypeEnum.TABLE.equals(node.getType())).collect(Collectors.toList());
    }

    public List<SqlParseNode> getTableList(SqlParseNodeActionEnum actionEnum) {
        return nodeMap.values().stream().filter(node -> node.getActions().contains(actionEnum)).collect(Collectors.toList());
    }

    public List<SqlParseNode> getViewList() {
        return nodeMap.values().stream().filter(node -> SqlParseNodeTypeEnum.VIEW.equals(node.getType())).collect(Collectors.toList());
    }

    public List<SqlParseNode> getInsertList() {
        return nodeMap.values().stream().filter(node -> SqlParseNodeTypeEnum.INSERT.equals(node.getType())).collect(Collectors.toList());
    }

    public List<SqlParseNode> getNodeList(SqlParseNodeTypeEnum sqlParseNodeTypeEnum) {
        return nodeMap.values().stream().filter(node -> node.getType().equals(sqlParseNodeTypeEnum)).collect(Collectors.toList());
    }

    public SqlParseInfo getSqlParseInfo() {
        return new SqlParseInfo(new ArrayList<>(this.nodeMap.values()), this.linkList);
    }

    public List<SqlParseDagNode> getDag() {
        Map<String, SqlParseDagNode> dagNodeMap = nodeMap.values().stream().map(SqlParseDagNode::new)
                .collect(Collectors.toMap(node -> node.getNode().getName(), Function.identity()));
        this.linkList.forEach(link -> dagNodeMap.get(link.getSourceName()).getChildList().add(dagNodeMap.get(link.getTargetName())));
        return dagNodeMap.values().stream().filter(node -> node.getNode().getActions().contains(SqlParseNodeActionEnum.SOURCE)).collect(Collectors.toList());
    }

    public static PlinkSqlParser create(String sql) throws PlinkSqlParseException {
        StreamExecutionEnvironment env = new LocalStreamEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        TableEnvironmentImpl tEnv = (TableEnvironmentImpl) StreamTableEnvironment.create(env, settings);
        SqlParser sqlParser = SqlParser.create(sql, sqlParserConfig);
        int currentLine = 1;
        int currentColumn = 1;
        try {
            SqlNodeList sqlNodes = sqlParser.parseStmtList();
            Map<String, SqlParseNode> nodeMap = new HashMap<>();
            List<SqlParseLink> linkList = new ArrayList<>();
            Map<String, Integer> insertNodeNumMap = new HashMap<>();
            String[] sqlLines = sql.split("\n");
            for (SqlNode sqlNode : sqlNodes) {
                SqlParserPos parserPosition;
                //这个是flink sql parser的bug需要额外处理下
                if (sqlNode instanceof SqlCreateView) {
                    parserPosition = sqlNode.getParserPosition().plus(((SqlCreateView) sqlNode).getQuery().getParserPosition());
                } else if (sqlNode instanceof RichSqlInsert) {
                    parserPosition = ((RichSqlInsert) sqlNode).getStaticPartitions().getParserPosition().plus(sqlNode.getParserPosition());
                } else {
                    parserPosition = sqlNode.getParserPosition();
                }
                currentLine = parserPosition.getLineNum();
                currentColumn = parserPosition.getColumnNum();
                String splitSql = getStringFromPos(sqlLines, parserPosition);
                if (splitSql.startsWith(";")) {
                    splitSql = splitSql.substring(1);
                }
                if (splitSql.endsWith(";")) {
                    splitSql = splitSql.substring(0, splitSql.length() - 1);
                }
                if (sqlNode instanceof SqlCreateTable) {
                    SqlCreateTable sqlCreateTable = (SqlCreateTable) sqlNode;
                    SqlParseNode node = new SqlParseNode();
                    node.setSql(splitSql);
                    node.setName(sqlCreateTable.getTableName().getSimple());
                    node.setType(SqlParseNodeTypeEnum.TABLE);
                    CreateTableOperation operation = (CreateTableOperation) tEnv.getParser().parse(splitSql).get(0);
                    List<SqlParseColumn> sqlParseColumnList = getColumnListFromTableSchema(operation.getCatalogTable().getSchema());
                    node.setColumnList(sqlParseColumnList);
                    Map<String, String> properties = sqlCreateTable.getPropertyList().getList().stream().map(x -> ((SqlTableOption) x))
                            .collect(Collectors.toMap(SqlTableOption::getKeyString, SqlTableOption::getValueString));
                    node.setProperties(properties);
                    node.setCalciteSqlNode(sqlNode);
                    if (sqlCreateTable.getComment().isPresent()) {
                        node.setComment(sqlCreateTable.getComment().get().toString());
                    } else {
                        node.setComment(sqlCreateTable.getTableName().toString());
                    }
                    nodeMap.put(node.getName(), node);
                } else if (sqlNode instanceof SqlCreateView) {
                    SqlCreateView sqlCreateView = (SqlCreateView) sqlNode;
                    String viewName = sqlCreateView.getViewName().getSimple();
                    CreateViewOperation createViewOperation = (CreateViewOperation) tEnv.getParser().parse(splitSql).get(0);
                    SqlParseNode node = new SqlParseNode();
                    node.setSql(splitSql);
                    node.setName(viewName);
                    node.setType(SqlParseNodeTypeEnum.VIEW);
                    List<SqlParseColumn> sqlParseColumnList = getColumnListFromTableSchema(createViewOperation.getCatalogView().getSchema());
                    node.setColumnList(sqlParseColumnList);
                    node.setCalciteSqlNode(sqlNode);
                    node.setComment(viewName);
                    nodeMap.put(node.getName(), node);

                    //获取上游查询的表组织成关系
                    Map<String, Set<SqlParseNodeActionEnum>> selectTableMap = lookupSelectTable(sqlCreateView.getQuery());
                    List<SqlParseLink> viewLinkList = selectTable2Link(selectTableMap.keySet(), viewName);
                    linkList.addAll(viewLinkList);

                    //设置tableNode的action
                    selectTableMap.forEach((tableName, actionSet) -> {
                        SqlParseNode sourNode = nodeMap.get(tableName);
                        if (SqlParseNodeTypeEnum.TABLE.equals(sourNode.getType())) {
                            sourNode.getActions().addAll(actionSet);
                        }
                    });
                } else if (sqlNode instanceof SqlInsert) {
                    SqlInsert sqlInsert = (SqlInsert) sqlNode;
                    String sinkTableName = sqlInsert.getTargetTable().toString();
                    String insertName = "insert_" + sinkTableName;
                    Integer insertNodeNum = insertNodeNumMap.get(sinkTableName);
                    if (insertNodeNum != null) {
                        insertName += "_" + insertNodeNum;
                    } else {
                        insertNodeNum = 0;
                    }
                    insertNodeNumMap.put(sinkTableName, ++insertNodeNum);
                    SqlParseNode node = new SqlParseNode();
                    node.setSql(splitSql);
                    node.setType(SqlParseNodeTypeEnum.INSERT);
                    node.setName(insertName);
                    CatalogSinkModifyOperation catalogSinkModifyOperation = (CatalogSinkModifyOperation) tEnv.getParser().parse(splitSql).get(0);
                    List<SqlParseColumn> sqlParseColumnList = getColumnListFromTableSchema(catalogSinkModifyOperation.getChild().getTableSchema());
                    node.setColumnList(sqlParseColumnList);
                    node.setCalciteSqlNode(sqlNode);
                    nodeMap.put(node.getName(), node);

                    //获取上游查询的表组织成关系
                    Map<String, Set<SqlParseNodeActionEnum>> selectTableMap = lookupSelectTable(sqlInsert.getSource());
                    List<SqlParseLink> sinkLinkList = selectTable2Link(selectTableMap.keySet(), insertName);
                    linkList.addAll(sinkLinkList);
                    linkList.add(new SqlParseLink(insertName, sinkTableName));

                    //设置tableNode的action
                    selectTableMap.forEach((tableName, actionSet) -> {
                        SqlParseNode sourceNode = nodeMap.get(tableName);
                        if (SqlParseNodeTypeEnum.TABLE.equals(sourceNode.getType())) {
                            sourceNode.getActions().addAll(actionSet);
                        }
                    });
                    SqlParseNode sinkNode = nodeMap.get(sinkTableName);
                    sinkNode.getActions().add(SqlParseNodeActionEnum.SINK);
                } else if (sqlNode instanceof SqlSetOption) {
                    String name = ((SqlSetOption) sqlNode).getName().getSimple();
                    String value = ((SqlSetOption) sqlNode).getValue().toString();
                    tEnv.getConfig().getConfiguration().setString(name, value);
                    continue;
                } else {
                    throw new RuntimeException("not support operation: " + sqlNode.getClass().getSimpleName());
                }
                tEnv.sqlUpdate(splitSql);
            }
            //校验
            tEnv.explain(true);
            return new PlinkSqlParser(nodeMap, linkList);
        } catch (Exception e) {
            Optional<CalciteContextException> calciteContextExceptionOptional = ExceptionUtils.findThrowable(e, CalciteContextException.class);
            if (calciteContextExceptionOptional.isPresent()) {
                CalciteContextException calciteContextException = calciteContextExceptionOptional.get();
                int line = currentLine + calciteContextException.getPosLine() - 1;
                int column = calciteContextException.getPosLine() == 1 ?
                        currentColumn + calciteContextException.getPosColumn() - 1 : calciteContextException.getPosColumn();
                int endLine = currentLine + calciteContextException.getEndPosLine() - 1;
                int endColumn = calciteContextException.getEndPosLine() == 1 ?
                        currentColumn + calciteContextException.getEndPosColumn() - 1 : calciteContextException.getEndPosColumn();
                throw new PlinkSqlParseException(calciteContextException.getCause().getMessage(), line, column, endLine, endColumn);
            }
            Optional<SqlParseException> SqlParseExceptionOptional = ExceptionUtils.findThrowable(e, SqlParseException.class);
            if (SqlParseExceptionOptional.isPresent()) {
                SqlParseException sqlParseException = SqlParseExceptionOptional.get();
                SqlParserPos pos = sqlParseException.getPos();
                throw new PlinkSqlParseException(sqlParseException.getMessage(),
                        pos.getLineNum(), pos.getColumnNum(), pos.getEndLineNum(), pos.getEndColumnNum());
            }
            throw new RuntimeException(e);
        }
    }

    private static List<SqlParseColumn> getColumnListFromTableSchema(TableSchema tableSchema) {
        return tableSchema.getTableColumns().stream().map(tableColumn -> {
            SqlParseColumn sqlParseColumn = new SqlParseColumn();
            sqlParseColumn.setName(tableColumn.getName());
            sqlParseColumn.setType(tableColumn.getType().getLogicalType().asSummaryString());
            sqlParseColumn.setNullable(tableColumn.getType().getLogicalType().isNullable());
            //sqlParseColumn.setIsPhysical(tableColumn.isPhysical());
            return sqlParseColumn;
        }).collect(Collectors.toList());
    }

    private static String getStringFromPos(String[] lines, SqlParserPos pos) {
        int lineNum = pos.getLineNum();
        int columnNum = pos.getColumnNum();
        int endLineNum = pos.getEndLineNum();
        int endColumnNum = pos.getEndColumnNum();
        StringBuilder sb = new StringBuilder();
        if (lineNum == endLineNum) {
            sb.append(lines[lineNum - 1], columnNum - 1, endColumnNum);
        } else {
            sb.append(lines[lineNum - 1].substring(columnNum - 1)).append("\n");
            for (int i = lineNum; i < endLineNum - 1; i++) {
                sb.append(lines[i]).append("\n");
            }
            sb.append(lines[endLineNum - 1], 0, endColumnNum);
        }
        return sb.toString();
    }

    private static List<SqlParseLink> selectTable2Link(Set<String> selectTableList, String target) {
        return selectTableList.stream().map(selectTable -> {
            SqlParseLink link = new SqlParseLink();
            link.setSourceName(selectTable);
            link.setTargetName(target);
            return link;
        }).collect(Collectors.toList());
    }

    private static Map<String, Set<SqlParseNodeActionEnum>> lookupSelectTable(SqlNode sqlNode, SqlParseNodeActionEnum action, Map<String, Set<SqlParseNodeActionEnum>> tableMap) {
        if (sqlNode instanceof SqlSelect) {
            SqlNode from = ((SqlSelect) sqlNode).getFrom();
            lookupSelectTable(from, action, tableMap);
        } else if (sqlNode instanceof SqlJoin) {
            SqlJoin sqlJoin = (SqlJoin) sqlNode;
            lookupSelectTable(sqlJoin.getLeft(), action, tableMap);
            lookupSelectTable(sqlJoin.getRight(), action, tableMap);
        } else if (sqlNode instanceof SqlSnapshot) {
            SqlSnapshot sqlSnapshot = (SqlSnapshot) sqlNode;
            lookupSelectTable(sqlSnapshot.getTableRef(), SqlParseNodeActionEnum.DIM, tableMap);
        } else if (sqlNode instanceof SqlBasicCall) {
            SqlBasicCall sqlBasicCall = (SqlBasicCall) sqlNode;
            SqlOperator operator = sqlBasicCall.getOperator();
            if (SqlKind.AS.equals(operator.getKind())) {
                lookupSelectTable(sqlBasicCall.getOperands()[0], action, tableMap);
            } else if (SqlKind.UNION.equals(operator.getKind())) {
                for (SqlNode operandSqlNode : sqlBasicCall.getOperands()) {
                    lookupSelectTable(operandSqlNode, action, tableMap);
                }
            } else {
                throw new RuntimeException("operator " + operator.getKind() + " not support");
            }
        } else if (sqlNode instanceof SqlIdentifier) {
            String tableName = ((SqlIdentifier) sqlNode).getSimple();
            if (tableMap.containsKey(tableName)) {
                tableMap.get(tableName).add(action);
            } else {
                Set<SqlParseNodeActionEnum> actionSet = new HashSet<>();
                actionSet.add(action);
                tableMap.put(tableName, actionSet);
            }
        } else {
            throw new RuntimeException("operator " + sqlNode.getClass() + " not support");
        }
        return tableMap;
    }

    private static Map<String, Set<SqlParseNodeActionEnum>> lookupSelectTable(SqlNode sqlNode) {
        Map<String, Set<SqlParseNodeActionEnum>> tableMap = new HashMap<>();
        lookupSelectTable(sqlNode, SqlParseNodeActionEnum.SOURCE, tableMap);
        return tableMap;
    }
}
