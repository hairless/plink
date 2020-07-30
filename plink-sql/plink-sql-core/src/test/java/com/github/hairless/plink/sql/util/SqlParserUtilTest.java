package com.github.hairless.plink.sql.util;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.sql.SqlJobTest;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.parser.SqlParseException;
import org.junit.Test;

/**
 * @author: silence
 * @date: 2020/7/24
 */
@Slf4j
public class SqlParserUtilTest {

    @Test
    public void parse() throws SqlParseException {
        log.info(SqlJobTest.sql);
        SqlParseInfo sqlParseInfo = SqlParserUtil.parse(SqlJobTest.sql);
        log.info(JSON.toJSONString(sqlParseInfo));
    }
}