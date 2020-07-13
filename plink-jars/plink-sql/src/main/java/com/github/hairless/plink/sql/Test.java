package com.github.hairless.plink.sql;

import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.flink.sql.parser.impl.FlinkSqlParserImpl;
import org.apache.flink.sql.parser.validate.FlinkSqlConformance;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.internal.TableEnvironmentImpl;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.delegation.Parser;
import org.apache.flink.table.delegation.Planner;

/**
 * @author: silence
 * @date: 2020/3/14
 */
public class Test {
    public static void main(String[] args) {
        // set up execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        TableEnvironmentImpl tEnv;
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        tEnv = (TableEnvironmentImpl) StreamTableEnvironment.create(env, settings);
        Planner planner = tEnv.getPlanner();
        Parser parser = planner.getParser();
        SqlParser.Config config = SqlParser
                .configBuilder()
                .setParserFactory(FlinkSqlParserImpl.FACTORY)
                .setConformance(FlinkSqlConformance.DEFAULT)
                .setLex(Lex.JAVA)
                .setIdentifierMaxLength(256)
                .build();

        String sql = "SET table.sql-dialect=hive;\ncreate table sinkTable(a int) with ('connector.type' = 'filesystem','format.type' = 'csv','connector.path' = 'xxx');" +
                "create table sinkTable2(a int) with ('connector.type' = 'filesystem','format.type' = 'csv','connector.path' = 'xxx');\n---234234";
        SqlParser sqlParser = SqlParser.create(sql, config);
        try {
            SqlNodeList sqlNodes = sqlParser.parseStmtList();
            System.out.println();
        } catch (SqlParseException e) {
            e.printStackTrace();
        }
       /* String[] split = sql.split(";");
        try {
            for (String sqlItem : split) {

                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String[] strings = tEnv.listTables();
        System.out.println();
    }
}
