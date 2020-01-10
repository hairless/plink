package com.github.hairless.plink.service;

import com.github.hairless.plink.model.pojo.PlinkTest;

import java.util.List;

/**
 * Created by silence on 2020/01/10
 */
public interface PlinkTestService {
    List<PlinkTest> selectAll();
}
