package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.PlinkSqlService;
import com.github.hairless.plink.sql.model.SqlDebugConfig;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/8/18
 */
@RestController
@RequestMapping("/mng/sql")
public class SqlController {
    @Autowired
    private PlinkSqlService plinkSqlService;


    /**
     * parse sql
     *
     * @param sql
     * @return
     */
    @RequestMapping("/parse")
    public Result<SqlParseInfo> parse(String sql) {
        return new Result<>(ResultCode.SUCCESS, plinkSqlService.parse(sql));
    }

    /**
     * debug sql
     *
     * @param sqlDebugConfig SqlDebugConfig
     * @return Result<Map < String, List < String>>>  map<tableName,resultData>
     */
    @PostMapping("/debug")
    public Result<Map<String, List<String>>> debug(@RequestBody SqlDebugConfig sqlDebugConfig) {
        return new Result<>(ResultCode.SUCCESS, plinkSqlService.debug(sqlDebugConfig));
    }
}
