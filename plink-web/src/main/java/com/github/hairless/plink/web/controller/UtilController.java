package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/27
 */
@RestController
@RequestMapping("/mng/util")
public class UtilController {
    @Autowired
    private UtilService utilService;

    @RequestMapping("/defaultFlinkConfs")
    Result<Map<String, String>> defaultFlinkConfs() {
        Map<String, String> defaultFlinkConfs = utilService.defaultFlinkConfs();
        return new Result<>(ResultCode.SUCCESS, defaultFlinkConfs);
    }
}
