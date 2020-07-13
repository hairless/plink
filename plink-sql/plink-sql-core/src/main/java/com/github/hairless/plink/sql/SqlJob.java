package com.github.hairless.plink.sql;

import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;
import org.apache.flink.sql.parser.validate.FlinkSqlConformance;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentImpl;
import org.apache.flink.table.operations.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: silence
 * @date: 2020/7/8
 */

public class SqlJob {
    private SqlConfig sqlConfig;

    public SqlJob(SqlConfig sqlConfig) {
        this.sqlConfig = sqlConfig;
    }

    public void start() throws Exception{
        StreamExecutionEnvironment env = new LocalStreamEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        TableEnvironmentImpl tEnv = (TableEnvironmentImpl) StreamTableEnvironment.create(env, settings);
        SqlParser.Config config = SqlParser
                .configBuilder()
                .setParserFactory(FlinkSqlParserImpl.FACTORY)
                .setConformance(FlinkSqlConformance.DEFAULT)
                .setLex(Lex.JAVA)
                .setIdentifierMaxLength(256)
                .build();

        SqlParser sqlParser = SqlParser.create(sqlConfig.getSql(), config);
        List<Operation> parse = new ArrayList<>();
        SqlNodeList sqlNodes = sqlParser.parseStmtList();
        sqlNodes.forEach(sqlNode -> {
            //parse.addAll(tEnv.getParser().parse(sqlNode.toString()));
            tEnv.sqlUpdate(sqlNode.toString());
        });
        Table temp_view = tEnv.from("temp_view");
        tEnv.execute("sql_job_test");
    }

    public static void main(String[] args) throws Exception{
        SqlConfig sqlConfig = new SqlConfig();
        sqlConfig.setSql("");

        SqlJob sqlJob = new SqlJob(sqlConfig);
        sqlJob.start();
    }
}
