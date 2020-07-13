package com.github.hairless.plink.sql;

import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentImpl;
import org.apache.flink.table.operations.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: silence
 * @date: 2020/7/13
 */
public class Test2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = new LocalStreamEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        TableEnvironmentImpl tEnv = (TableEnvironmentImpl) StreamTableEnvironment.create(env, settings);

        List<Operation> parse = new ArrayList<>();
        tEnv.sqlUpdate("create table t1(a int comment '测试',b string,c int) with ( 'connector.type' = 'COLLECTION');");
        /*tEnv.sqlUpdate("create table t2( a int,b string, c int) with ( 'connector.type' = 'COLLECTION')");
        tEnv.sqlUpdate("insert into t2 select t1.a, t1.b, t1.a + 3 as c from t1");*/
        tEnv.execute("sql_job_test");
    }
}
