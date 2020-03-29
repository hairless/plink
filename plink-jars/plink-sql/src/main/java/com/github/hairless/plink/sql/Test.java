package com.github.hairless.plink.sql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;

/**
 * @author: silence
 * @date: 2020/3/14
 */
public class Test {
    public static void main(String[] args) {

        // set up execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tEnv;
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        tEnv = StreamTableEnvironment.create(env, settings);
        tEnv.sqlUpdate("create table sinkTable(a int,b varchar,) with ('connector.type' = 'filesystem','format.type' = 'csv','connector.path' = 'xxx')");
    }
}
