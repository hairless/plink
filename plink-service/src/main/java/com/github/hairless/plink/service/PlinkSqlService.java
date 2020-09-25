package com.github.hairless.plink.service;

import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;

/**
 * @author: silence
 * @date: 2020/9/24
 */
public interface PlinkSqlService {

    SqlParseInfo parse(String sql);

}
