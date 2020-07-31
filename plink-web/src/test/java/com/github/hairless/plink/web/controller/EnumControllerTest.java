package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.TestBootStrapApp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: silence
 * @date: 2020/2/3
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestBootStrapApp.class})
public class EnumControllerTest extends BaseControllerTest {

    @Test
    public void jobInstanceStatus() throws Exception {
        getTest("/mng/enum/jobInstanceStatus");
    }

    @Test
    public void jobType() throws Exception {
        getTest("/mng/enum/jobType");
    }
}