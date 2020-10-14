package com.github.hairless.plink.sql;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.hairless.plink.sql.model.SqlConfig;
import com.github.hairless.plink.sql.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: silence
 * @date: 2020/3/14
 */
@Slf4j
public class SqlJobTest {
    public static String sql;

    static {
        List<String> sourceData = Stream.of(
                JsonNodeFactory.instance.objectNode().put("a", 1).put("b", "1000").put("c", 2),
                JsonNodeFactory.instance.objectNode().put("a", 2).put("b", "6000").put("c", 2),
                JsonNodeFactory.instance.objectNode().put("a", 3).put("b", "2000").put("c", 4),
                JsonNodeFactory.instance.objectNode().put("a", 4).put("b", "1000").put("c", 2),
                JsonNodeFactory.instance.objectNode().put("a", 5).put("b", "5000").put("c", 3),
                JsonNodeFactory.instance.objectNode().put("a", 6).put("b", "3000").put("c", 2)
        ).map(JsonUtil::toJSONString).collect(Collectors.toList());

        String sourceDDL =
                "set a=b;create table t1( a int,b string, c int) with ( 'connector' = 'collection','data'='" + JsonUtil.toJSONString(sourceData) + "');";
        String sinkDDL =
                "create table t2(a int comment '测试',b string,c int) with ( 'connector' = 'print');";
        String viewSql =
                "create view temp_view as select * from t1;";
        String query =
                "insert into t2(a,b,c) select a,b,c from temp_view t1;insert into t2(a,b,c) select a,b,c from temp_view t1;";

        sql = sourceDDL + sinkDDL + viewSql + query;
    }

    @Test
    public void sqlJobTest() {

        Exception exception = null;
        try {
            SqlConfig config = SqlConfig.builder().sql(sql).jobName("sql_job_test").build();
            SqlJob sqlJob = new SqlJob(config);
            sqlJob.start();
        } catch (Exception e) {
            log.error("sqlJobTest error", e);
            exception = e;
        }
        assert exception == null;
    }

    @Test
    public void sqlJobWatermarkTest() {
        List<String> sourceData = Stream.of(
                JsonNodeFactory.instance.objectNode().put("data_time", "2020-01-01 12:00:01"),
                JsonNodeFactory.instance.objectNode().put("data_time", "2020-01-01 12:00:02"),
                JsonNodeFactory.instance.objectNode().put("data_time", "2020-01-01 12:00:03"),
                JsonNodeFactory.instance.objectNode().put("data_time", "2020-01-01 12:01:01")
        ).map(JsonUtil::toJSONString).collect(Collectors.toList());
        String sql = "create table t1( " +
                "data_time STRING, " +
                "row1_time AS to_timestamp(data_time)," +
                "WATERMARK FOR row1_time AS row1_time - INTERVAL '5' SECOND " +
                ") with ( 'connector' = 'collection','data'='" + JsonUtil.toJSONString(sourceData) + "');" +
                "create table t2(stime TIMESTAMP(3),cnt bigint) with ( 'connector' = 'print');" +
                "insert into t2 select TUMBLE_START(row1_time, INTERVAL '1' MINUTE) as stime,count(1) cnt from t1 group by TUMBLE(row1_time, INTERVAL '1' MINUTE);;";


        Exception exception = null;
        try {
            SqlConfig config = SqlConfig.builder().sql(sql).jobName("sql_job_test").build();
            SqlJob sqlJob = new SqlJob(config);
            sqlJob.start();
        } catch (Exception e) {
            log.error("sqlJobTest error", e);
            exception = e;
        }
        assert exception == null;
    }


}