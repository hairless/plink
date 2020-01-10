package com.github.hairless.plink.service;

import com.github.hairless.plink.model.pojo.PlinkTest;
import com.github.pagehelper.PageInfo;

/**
 * Created by silence on 2020/01/10
 */
public interface PlinkTestService {
    PageInfo<PlinkTest> selectAll();
}
