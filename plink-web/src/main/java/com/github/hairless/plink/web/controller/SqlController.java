package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.PlinkSqlService;
import com.github.hairless.plink.sql.model.sqlparse.SqlParseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
