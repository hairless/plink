package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: silence
 * @date: 2020/8/18
 */
@RestController
@RequestMapping("/mng/sql")
public class SqlController {

    /**
     * 添加作业
     *
     * @param jobDTO 作业请求对象
     */
    @RequestMapping("/graph")
    public Result<Object> addJob(@RequestBody JobDTO jobDTO) {

        return new Result<>(ResultCode.SUCCESS, new Object());
    }
}
