package com.github.hairless.plink.sql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.sql.model.SqlConfig;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: silence
 * @date: 2020/3/14
 */
public class SqlJobTest {
    public static String sql;

    static {
        List<JSONObject> sourceData = Stream.of(
                new JSONObject().fluentPut("a", 1).fluentPut("b", "1000").fluentPut("c", 2),
                new JSONObject().fluentPut("a", 2).fluentPut("b", "1000").fluentPut("c", 3),
                new JSONObject().fluentPut("a", 3).fluentPut("b", "2000").fluentPut("c", 4),
                new JSONObject().fluentPut("a", 4).fluentPut("b", "2000").fluentPut("c", 5),
                new JSONObject().fluentPut("a", 5).fluentPut("b", "3000").fluentPut("c", 6)
        ).collect(Collectors.toList());

        String sourceDDL =
                "create table t1( a int,b string, c int) with ( 'connector' = 'collection','data'='" + JSON.toJSONString(sourceData) + "','format.type'='json');";
        String sinkDDL =
                "create table t2(a int comment '测试',b string,c int) with ( 'connector' = 'collection', 'format.type'='json');";
        String viewSql =
                "create view temp_view as select * from t1;";
        String query =
                "insert into t2 select t1.a, t1.b, t1.a + 3 as c from temp_view t1;";

        sql = sourceDDL + sinkDDL + viewSql + query;
    }

    @Test
    public void sqlJobTest() throws Exception {
        SqlConfig config = SqlConfig.builder().sql(sql).jobName("sql_job_test").build();
        SqlJob sqlJob = new SqlJob(config);
        sqlJob.start();
    }


}