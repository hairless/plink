package com.github.hairless.plink.service;

import com.github.hairless.plink.sql.model.SqlDebugConfig;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;

import java.util.List;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/9/24
 */
public interface PlinkSqlService {

    SqlParseInfo parse(String sql);

    Map<String, List<String>> debug(SqlDebugConfig sqlDebugConfig);
}
