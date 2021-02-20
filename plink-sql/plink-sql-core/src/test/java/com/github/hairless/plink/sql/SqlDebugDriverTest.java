package com.github.hairless.plink.sql;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.hairless.plink.sql.model.SqlDebugConfig;
import com.github.hairless.plink.sql.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: silence
 * @date: 2020/7/30
 */
@Slf4j
public class SqlDebugDriverTest {
    @Test
    public void debug() throws Exception {
        String sql =
                "create table t1( a int,b string, c int) with ( 'connector' = 'collection','data'='[]');" +
                        "create table t2(a int comment '测试',b string,c int) with ( 'connector' = 'print');" +
                        "create view temp_view as select * from t1;" +
                        "insert into t2(a,b,c) select a,b,c from temp_view t1;" +
                        "insert into t2(a,b,c) select a,b,c from temp_view t1;";
        List<String> sourceData = Stream.of(
                JsonNodeFactory.instance.objectNode().put("a", 1).put("b", "1000").put("c", 2),
                JsonNodeFactory.instance.objectNode().put("a", 2).put("b", "6000").put("c", 2),
                JsonNodeFactory.instance.objectNode().put("a", 3).put("b", "2000").put("c", 4),
                JsonNodeFactory.instance.objectNode().put("a", 4).put("b", "1000").put("c", 2),
                JsonNodeFactory.instance.objectNode().put("a", 5).put("b", "5000").put("c", 3),
                JsonNodeFactory.instance.objectNode().put("a", 6).put("b", "3000").put("c", 2)
        ).map(JsonUtil::toJSONString).collect(Collectors.toList());
        SqlDebugConfig sqlDebugConfig = new SqlDebugConfig();
        HashMap<String, SqlDebugConfig.SourceConfig> sourceConfigMap = new HashMap<>();
        SqlDebugConfig.SourceConfig sourceConfig = new SqlDebugConfig.SourceConfig();
        sourceConfig.setData(sourceData);
        sourceConfigMap.put("t1", sourceConfig);
        sqlDebugConfig.setSourceConfigMap(sourceConfigMap);
        sqlDebugConfig.setSql(sql);
        log.info("sqlDebugConfig={}", JsonUtil.toJSONString(sqlDebugConfig));
        Map<String, List<String>> debugResult = SqlDebugDriver.debug(sqlDebugConfig);
        log.info("sqlDebugResult={}", JsonUtil.toJSONString(debugResult));
        assert MapUtils.isNotEmpty(debugResult);
    }

    @Test
    public void debugDatagen() throws Exception {
        String sql =
                "create table t1( a int,b string, c int) with ( 'connector' = 'collection','data'='[]');" +
                        "create table t2(a int comment '测试',b string,c int) with ( 'connector' = 'print');" +
                        "create view temp_view as select * from t1;" +
                        "insert into t2(a,b,c) select a,b,c from temp_view t1;" +
                        "insert into t2(a,b,c) select a,b,c from temp_view t1;";
        SqlDebugConfig sqlDebugConfig = new SqlDebugConfig();
        HashMap<String, SqlDebugConfig.SourceConfig> sourceConfigMap = new HashMap<>();
        SqlDebugConfig.SourceConfig sourceConfig = new SqlDebugConfig.SourceConfig();
        sourceConfig.setDatagen(true);
        sourceConfig.setLimit(10);
        sourceConfigMap.put("t1", sourceConfig);
        sqlDebugConfig.setSourceConfigMap(sourceConfigMap);
        sqlDebugConfig.setSql(sql);
        log.info("sqlDebugConfig={}", JsonUtil.toJSONString(sqlDebugConfig));
        Map<String, List<String>> debugResult = SqlDebugDriver.debug(sqlDebugConfig);
        assert MapUtils.isNotEmpty(debugResult);
    }

    @Test
    public void debugWatermark() throws Exception {
        String sql = "create table t1( " +
                "data_time STRING ," +
                "row1_time AS to_timestamp(data_time)," +
                "WATERMARK FOR row1_time AS row1_time - INTERVAL '5' SECOND " +
                ") with ( 'connector' = 'collection','data'='[]');" +
                "create table t2(stime TIMESTAMP(3),cnt bigint) with ( 'connector' = 'print');" +
                "insert into t2 select TUMBLE_START(row1_time, INTERVAL '1' MINUTE) as stime,count(1) cnt from t1 group by TUMBLE(row1_time, INTERVAL '1' MINUTE);";
        List<String> sourceData = Stream.of(
                JsonNodeFactory.instance.objectNode().put("data_time", "2020-01-01 12:00:01"),
                JsonNodeFactory.instance.objectNode().put("data_time", "2020-01-01 12:00:02"),
                JsonNodeFactory.instance.objectNode().put("data_time", "2020-01-01 12:00:03"),
                JsonNodeFactory.instance.objectNode().put("data_time", "2020-01-01 12:01:01")
        ).map(JsonUtil::toJSONString).collect(Collectors.toList());
        SqlDebugConfig sqlDebugConfig = new SqlDebugConfig();
        HashMap<String, SqlDebugConfig.SourceConfig> sourceConfigMap = new HashMap<>();
        SqlDebugConfig.SourceConfig sourceConfig = new SqlDebugConfig.SourceConfig();
        sourceConfig.setData(sourceData);
        sourceConfigMap.put("t1", sourceConfig);
        sqlDebugConfig.setSourceConfigMap(sourceConfigMap);
        sqlDebugConfig.setSql(sql);
        log.info("sqlDebugConfig={}", JsonUtil.toJSONString(sqlDebugConfig));
        Map<String, List<String>> debugResult = SqlDebugDriver.debug(sqlDebugConfig);
        List<String> t2 = debugResult.get("t2");
        assert t2 != null && t2.size() > 0;
        assert "{\"stime\":\"2020-01-01 12:00:00\",\"cnt\":3}".equals(t2.get(0));
        assert "{\"stime\":\"2020-01-01 12:01:00\",\"cnt\":1}".equals(t2.get(1));
    }
}