package com.github.hairless.plink.sql;

import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.sql.model.SqlDebugConfig;
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
public class SqlDebugDriverTest {
    private static List<String> sourceData;

    static {
        sourceData = Stream.of(
                new JSONObject().fluentPut("a", 1).fluentPut("b", "1000").fluentPut("c", 2).toJSONString(),
                new JSONObject().fluentPut("a", 2).fluentPut("b", "1000").fluentPut("c", 3).toJSONString(),
                new JSONObject().fluentPut("a", 3).fluentPut("b", "2000").fluentPut("c", 4).toJSONString(),
                new JSONObject().fluentPut("a", 4).fluentPut("b", "2000").fluentPut("c", 5).toJSONString(),
                new JSONObject().fluentPut("a", 5).fluentPut("b", "3000").fluentPut("c", 6).toJSONString()
        ).collect(Collectors.toList());
    }

    @Test
    public void debug() throws Exception {
        SqlDebugConfig sqlDebugConfig = new SqlDebugConfig();
        HashMap<String, SqlDebugConfig.SourceConfig> sourceConfigMap = new HashMap<>();
        sourceConfigMap.put("t1", new SqlDebugConfig.SourceConfig(sourceData));
        sqlDebugConfig.setMap(sourceConfigMap);
        Map<String, List<String>> debugResult = SqlDebugDriver.debug(SqlJobTest.sql, sqlDebugConfig);
        assert MapUtils.isNotEmpty(debugResult);
    }
}