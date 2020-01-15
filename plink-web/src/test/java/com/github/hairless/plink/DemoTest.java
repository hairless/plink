package com.github.hairless.plink;

import com.github.hairless.plink.dao.mapper.PlinkTestMapper;
import com.github.hairless.plink.model.pojo.PlinkTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: silence
 * @date: 2020/1/14
 */
@Slf4j
public class DemoTest extends BaseTest {

    @Autowired
    private PlinkTestMapper plinkTestMapper;

    @Test
    public void demoTest() {
        List<PlinkTest> plinkTestList = plinkTestMapper.selectAll();
        log.info(plinkTestList.toString());
    }
}
