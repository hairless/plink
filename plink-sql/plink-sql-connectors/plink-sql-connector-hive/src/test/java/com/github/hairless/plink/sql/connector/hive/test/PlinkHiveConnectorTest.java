package com.github.hairless.plink.sql.connector.hive.test;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author chaixiaoxue
 * @date 2021/3/19 11:20
 * 这个是测试demo
 */
public class PlinkHiveConnectorTest {

    public static void main(String[] args) {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(env,settings);

        streamTableEnvironment.executeSql("CREATE TABLE goods_info_df2 (\n"
            + "  name STRING,\n"
            + "  age string,"
            + "  dt string,"
            + "  h string\n"
            + ") PARTITIONED BY (dt,h) WITH (\n"
            + "  'connector' = 'plink-hive',"
            +"    'hive.conf.dir'='...',"
            + "     'hive.database'='tmp',"
            + "     'hive.table' = 'goods_info_df2',"
            + "  'sink.partition-commit.policy.kind'='metastore,success-file'\n"
            + ")");

        streamTableEnvironment.executeSql("CREATE TABLE ck (\n"
            + "  batch_id string,\n"
            + "  message string,\n"
            + "  `timestamp` bigint,\n"
            + "  md5 string,\n"
            + "  es bigint,\n"
            +"   ts AS TO_TIMESTAMP(FROM_UNIXTIME(es / 1000, 'yyyy-MM-dd HH:mm:ss'))   "
            + "  ) WITH (\n"
            + " 'connector' = 'kafka',\n"
            + " 'topic' = 'test',\n"
            + " 'properties.bootstrap.servers' = '127.0.0.1:9092',\n"
            + " 'properties.group.id' = 'kafka_to_hive',\n"
            + " 'scan.startup.mode' = 'group-offsets',\n"
            + " 'format' = 'json',\n"
            + " 'json.fail-on-missing-field' = 'false',\n"
            + " 'json.ignore-parse-errors' = 'true'\n"
            + ")");

        streamTableEnvironment.executeSql("insert into goods_info_df2 \n"
            + "SELECT \n"
            + "batch_id,\n"
            + "message,DATE_FORMAT(ts, 'yyyy-MM-dd'), DATE_FORMAT(ts, 'HH')\n"
            + "FROM ck");
    }
}
