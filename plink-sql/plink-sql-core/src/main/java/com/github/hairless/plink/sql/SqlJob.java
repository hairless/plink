package com.github.hairless.plink.sql;

import com.github.hairless.plink.sql.model.SqlConfig;
import com.github.hairless.plink.sql.util.SkipAnsiCheckSqlDialect;
import com.github.hairless.plink.sql.util.SqlParserUtil;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.parser.SqlParser;
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


    public void start() throws Exception {
        StreamExecutionEnvironment env = new LocalStreamEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        TableEnvironmentImpl tEnv = (TableEnvironmentImpl) StreamTableEnvironment.create(env, settings);

        SqlParser sqlParser = SqlParser.create(sqlConfig.getSql(), SqlParserUtil.sqlParserConfig);
        SqlNodeList sqlNodes = sqlParser.parseStmtList();
        sqlNodes.forEach(sqlNode -> {
            String sql = sqlNode.toSqlString(SkipAnsiCheckSqlDialect.DEFAULT).getSql();
            tEnv.sqlUpdate(sql);
        });
        tEnv.execute(sqlConfig.getJobName());
    }

}
