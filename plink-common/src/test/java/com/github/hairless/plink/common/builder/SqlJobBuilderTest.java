package com.github.hairless.plink.common.builder;

import org.junit.Test;

public class SqlJobBuilderTest {

    @Test
    public void testBuildFlinkSubmitOption() throws Exception {
        String extraConfigStr = "{\"sql\":\"CREATE TABLE kafka_cxx_test (\\n   u_utrace string,\\n  `timestamp` string,\\n  ts AS TO_TIMESTAMP(FROM_UNIXTIME(cast(`timestamp`as bigint), 'yyyy-MM-dd HH:mm:ss'))\\n  --PRIMARY KEY (u_utrace) NOT ENFORCED\\n -- ts AS TO_TIMESTAMP(FROM_UNIXTIME(`timestamp` / 1000, 'yyyy-MM-dd HH:mm:ss'))\\n) WITH (\\n    'connector' = 'kafka',\\n    'topic' = 'cxx_test',\\n    'properties.group.id' = 'cxx',\\n    'scan.startup.mode' = 'latest-offset',\\n    'properties.bootstrap.servers' = 'yx-dc-3-100:9092',\\n    'format' = 'weblog-json'\\n);\\nCREATE TABLE kafka_cxx_test_2 (\\n  u_utrace string,\\n `timestamp` string,\\n ts TIMESTAMP(3)\\n) WITH (\\n    'connector' = 'kafka',\\n    'topic' = 'kafka_cxx_test_2',\\n    'scan.startup.mode' = 'earliest-offset',\\n    'properties.bootstrap.servers' = 'yx-dc-3-100:9092',\\n    'format' = 'json'\\n);\\ninsert into  kafka_cxx_test_2\\nSELECT u_utrace,`timestamp`,ts from kafka_cxx_test ;\\n--SELECT count(DISTINCT u_utrace) as u_num from kafka_cxx_test group by ;\"}";
        System.out.println(extraConfigStr.replace("`", "\\`"));
    }
}
