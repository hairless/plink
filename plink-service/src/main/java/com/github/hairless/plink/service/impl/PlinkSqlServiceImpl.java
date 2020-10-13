package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.util.PlinkSqlUtil;
import com.github.hairless.plink.service.PlinkSqlService;
import com.github.hairless.plink.sql.model.SqlDebugConfig;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/9/24
 */
@Slf4j
@Service
public class PlinkSqlServiceImpl implements PlinkSqlService {

    /**
     * 调用plink-sql-core模块的{@link com.github.hairless.plink.sql.util.PlinkSqlParser}
     * PlinkSqlParser.create(sql).getSqlParseInfo()
     *
     * @param sql flink sql
     * @return SqlParseInfo {@link com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo}
     */
    @Override
    public SqlParseInfo parse(String sql) {
        return PlinkSqlUtil.parse(sql);
    }

    /**
     * 调用plink-sql-core模块的{@link com.github.hairless.plink.sql.SqlDebugDriver}
     * SqlDebugDriver.debug(sqlDebugConfig)
     *
     * @param sqlDebugConfig SqlDebugConfig
     * @return Map<String, List < String>>
     */
    @Override
    public Map<String, List<String>> debug(SqlDebugConfig sqlDebugConfig) {
        return PlinkSqlUtil.debug(sqlDebugConfig);
    }

}
