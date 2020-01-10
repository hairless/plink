package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.dao.mapper.PlinkTestMapper;
import com.github.hairless.plink.model.pojo.PlinkTest;
import com.github.hairless.plink.service.PlinkTestService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by silence on 2020/01/10
 */
@Slf4j
@Service
public class PlinkTestServiceImpl implements PlinkTestService {
    @Autowired
    private PlinkTestMapper plinkTestMapper;

    public PageInfo<PlinkTest> selectAll() {
        PageHelper.startPage(1, 1);
        PageInfo<PlinkTest> plinkTestPageInfo = new PageInfo<>(plinkTestMapper.selectAll());
        log.info("log test:{}", plinkTestPageInfo);
        return plinkTestPageInfo;
    }
}
