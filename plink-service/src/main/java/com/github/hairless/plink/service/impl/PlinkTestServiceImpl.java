package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.dao.mapper.PlinkTestMapper;
import com.github.hairless.plink.model.pojo.PlinkTest;
import com.github.hairless.plink.service.PlinkTestService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by silence on 2020/01/10
 */
@Service
public class PlinkTestServiceImpl implements PlinkTestService {
    @Autowired
    private PlinkTestMapper plinkTestMapper;

    public List<PlinkTest> selectAll() {
        PageHelper.startPage(1, 1);
        List<PlinkTest> plinkTestList = plinkTestMapper.selectAll();
        return plinkTestList;
    }
}
