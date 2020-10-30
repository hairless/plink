package com.github.hairless.plink.common.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.hairless.plink.common.util.JsonUtil;
import com.github.hairless.plink.common.util.PlinkSqlUtil;
import com.github.hairless.plink.common.util.PlinkUtil;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.sql.model.SqlConfig;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.flink.util.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/13
 */
public class SqlJobBuilder implements JobBuilder {

    @Override
    public void validate(JobDTO jobDTO) {
        Preconditions.checkNotNull(jobDTO, "jobDTO is null");
        JsonNode extraConfig = jobDTO.getExtraConfig();
        Preconditions.checkNotNull(extraConfig, "extraConfig is null");
        JsonNode sql = extraConfig.get("sql");
        Preconditions.checkNotNull(sql, "sql is null");
        String sqlString = sql.textValue();
        Preconditions.checkArgument(StringUtils.isNotBlank(sqlString), "sql is empty");
        SqlParseInfo sqlParseInfo = PlinkSqlUtil.parse(sqlString);
        Preconditions.checkNotNull(sqlParseInfo, "sqlParseInfo is null");
    }

    @Override
    public FlinkSubmitOptions buildFlinkSubmitOption(JobInstanceDTO jobInstanceDTO) {
        String jobName = "PLINK_SQL_" + jobInstanceDTO.getJob().getName();
        FlinkSubmitOptions flinkSubmitOptions = new FlinkSubmitOptions();
        flinkSubmitOptions.setJobName(jobName);
        flinkSubmitOptions.setMainJarPath(PlinkUtil.getPlinkHome() + PlinkSqlUtil.SQL_JAR_FILE);
        FlinkConfig flinkConfig = jobInstanceDTO.getFlinkConfig();
        flinkConfig.setMainClass(PlinkSqlUtil.PLINK_SQL_JOB_DRIVER_CLASS_NAME);
        SqlConfig sqlConfig = new SqlConfig();
        sqlConfig.setJobName(jobName);
        sqlConfig.setSql(jobInstanceDTO.getExtraConfig().get("sql").textValue());
        List<String> args = new ArrayList<>();
        args.add("\"-c\"");
        args.add('"' + StringEscapeUtils.escapeJava(JsonUtil.toJSONString(sqlConfig)) + '"');
        flinkConfig.setArgs(String.join(" ", args));
        Map<String, String> configs = flinkConfig.getConfigs();
        configs.forEach((k, v) -> {
            if (!configs.containsKey(k)) {
                configs.put(k, v);
            }
        });
        flinkSubmitOptions.setFlinkConfig(flinkConfig);
        return flinkSubmitOptions;
    }
}
