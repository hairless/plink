package com.github.hairless.plink.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author: silence
 * @date: 2020/2/3
 */
@Slf4j
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