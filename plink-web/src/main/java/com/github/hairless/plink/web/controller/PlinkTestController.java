package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.pojo.PlinkTest;
import com.github.hairless.plink.service.PlinkTestService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by silence on 2020/01/10
 */
@RestController
public class PlinkTestController {
    @Autowired
    private PlinkTestService plinkTestService;

    @RequestMapping("plinkTest")
    public PageInfo<PlinkTest> plinkTest() {
        return plinkTestService.selectAll();
    }
}
