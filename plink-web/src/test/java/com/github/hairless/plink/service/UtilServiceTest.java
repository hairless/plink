package com.github.hairless.plink.service;

import com.github.hairless.plink.TestBootStrapApp;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestBootStrapApp.class})
public class UtilServiceTest {

    @Autowired
    UtilService utilService;

    @Test
    public void defaultFlinkConfs() {
        Map<String, String> defaultFlinkConfs = utilService.defaultFlinkConfs();
        assert MapUtils.isNotEmpty(defaultFlinkConfs);
    }
}