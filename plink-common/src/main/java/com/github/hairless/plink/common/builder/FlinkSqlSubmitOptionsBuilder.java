package com.github.hairless.plink.common.builder;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.common.util.PlinkSqlUtil;
import com.github.hairless.plink.common.util.PlinkUtil;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.sql.model.SqlConfig;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: silence
 * @date: 2020/10/13
 */
public class FlinkSqlSubmitOptionsBuilder implements FlinkSubmitOptionsBuilder {
    @Override
    public FlinkSubmitOptions builder(JobInstanceDTO jobInstanceDTO) {
        String jobName = "PLINK_SQL_" + jobInstanceDTO.getJob().getName();
        FlinkSubmitOptions flinkSubmitOptions = new FlinkSubmitOptions();
        flinkSubmitOptions.setJobName(jobName);
        flinkSubmitOptions.setMainJarPath(PlinkUtil.getPlinkHome() + PlinkSqlUtil.SQL_JAR_FILE);
        FlinkConfig flinkConfig = jobInstanceDTO.getFlinkConfig();
        flinkConfig.setMainClass(PlinkSqlUtil.PLINK_SQL_JOB_DRIVER_CLASS_NAME);
        SqlConfig sqlConfig = new SqlConfig();
        sqlConfig.setJobName(jobName);
        sqlConfig.setSql(jobInstanceDTO.getExtraConfig().getString("sql"));
        List<String> args = new ArrayList<>();
        args.add("-c");
        args.add(StringEscapeUtils.escapeJava(JSON.toJSONString(sqlConfig)));
        flinkConfig.setArgs(String.join(" ", args));
        flinkSubmitOptions.setFlinkConfig(flinkConfig);
        return flinkSubmitOptions;
    }
}
