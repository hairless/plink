package com.github.hairless.plink.sql;

import com.github.hairless.plink.sql.model.SqlConfig;
import com.github.hairless.plink.sql.util.PlinkSqlParser;
import com.github.hairless.plink.sql.util.SkipAnsiCheckSqlDialect;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSetOption;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentImpl;

/**
 * @author: silence
 * @date: 2020/7/8
 */

public class SqlJob {
    private final SqlConfig sqlConfig;

    public SqlJob(SqlConfig sqlConfig) {
        this.sqlConfig = sqlConfig;
    }


    public JobExecutionResult start() throws Exception {
        StreamExecutionEnvironment env = new LocalStreamEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        TableEnvironmentImpl tEnv = (TableEnvironmentImpl) StreamTableEnvironment.create(env, settings);

        SqlParser sqlParser = SqlParser.create(sqlConfig.getSql(), PlinkSqlParser.sqlParserConfig);
        SqlNodeList sqlNodes = sqlParser.parseStmtList();
        sqlNodes.forEach(sqlNode -> {
            if (sqlNode instanceof SqlSetOption) {
                String name = ((SqlSetOption) sqlNode).getName().getSimple();
                String value = ((SqlSetOption) sqlNode).getValue().toString();
                tEnv.getConfig().getConfiguration().setString(name, value);
            } else {
                String sql = sqlNode.toSqlString(SkipAnsiCheckSqlDialect.DEFAULT).getSql();
                tEnv.sqlUpdate(sql);
            }
        });
        return tEnv.execute(sqlConfig.getJobName());
    }

}
