package com.github.hairless.plink.sql.util;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.sql.SqlJobTest;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseDagNode;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.parser.SqlParseException;
import org.junit.Test;

import java.util.List;

/**
 * @author: silence
 * @date: 2020/7/24
 */
@Slf4j
public class PlinkSqlParserTest {

    @Test
    public void parse() throws SqlParseException {
        log.info(SqlJobTest.sql);
        PlinkSqlParser plinkSqlParser = PlinkSqlParser.create(SqlJobTest.sql);
        SqlParseInfo sqlParseInfo = plinkSqlParser.getSqlParseInfo();
        log.info("sqlParseInfo:{}", JSON.toJSONString(sqlParseInfo));
        List<SqlParseDagNode> dag = plinkSqlParser.getDag();
        log.info("dag:{}", JSON.toJSONString(dag));
    }
}