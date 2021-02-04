package com.github.hairless.plink.common.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.hairless.plink.common.util.FileUtil;
import com.github.hairless.plink.common.util.JsonUtil;
import com.github.hairless.plink.common.util.PlinkSqlUtil;
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

/**
 * @author: silence
 * @date: 2020/10/13
 */
public class SqlJobBuilder extends JobBuilder {

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
    public FlinkSubmitOptions buildFlinkSubmitOptionInternal(JobInstanceDTO jobInstanceDTO) {
        String jobName = "PLINK_SQL_" + jobInstanceDTO.getJob().getName();
        FlinkSubmitOptions flinkSubmitOptions = new FlinkSubmitOptions();
        flinkSubmitOptions.setJobName(jobName);
        flinkSubmitOptions.setMainJarPath(PlinkSqlUtil.SQL_JAR_FILE);
        //设置依赖&本地classpath
        List<String> shapeFiles = new ArrayList<>();
        List<String> classPaths = new ArrayList<>();
        if (FileUtil.exists(PlinkSqlUtil.SQL_CONNECTORS_DIR_PATH)) {
            shapeFiles.add(PlinkSqlUtil.SQL_CONNECTORS_DIR_PATH);
            classPaths.addAll(FileUtil.listFileNames(PlinkSqlUtil.SQL_CONNECTORS_DIR_PATH));
        }
        if (FileUtil.exists(PlinkSqlUtil.SQL_FORMATS_DIR_PATH)) {
            shapeFiles.add(PlinkSqlUtil.SQL_FORMATS_DIR_PATH);
            classPaths.addAll(FileUtil.listFileNames(PlinkSqlUtil.SQL_FORMATS_DIR_PATH));
        }
        if (FileUtil.exists(PlinkSqlUtil.SQL_UDF_DIR_PATH)) {
            shapeFiles.add(PlinkSqlUtil.SQL_UDF_DIR_PATH);
            classPaths.addAll(FileUtil.listFileNames(PlinkSqlUtil.SQL_UDF_DIR_PATH));
        }
        flinkSubmitOptions.setShapefiles(shapeFiles);
        flinkSubmitOptions.setLocalClasspath(classPaths);

        FlinkConfig flinkConfig = jobInstanceDTO.getFlinkConfig();
        flinkConfig.setMainClass(PlinkSqlUtil.PLINK_SQL_JOB_DRIVER_CLASS_NAME);
        SqlConfig sqlConfig = new SqlConfig();
        sqlConfig.setJobName(jobName);
        sqlConfig.setSql(jobInstanceDTO.getExtraConfig().get("sql").textValue());
        List<String> args = new ArrayList<>();
        args.add("\"-c\"");
        args.add('"' + StringEscapeUtils.escapeJava(JsonUtil.toJSONString(sqlConfig)) + '"');
        flinkConfig.setArgs(String.join(" ", args).replace("`", "\\`"));
        flinkSubmitOptions.setFlinkConfig(flinkConfig);
        return flinkSubmitOptions;
    }
}
