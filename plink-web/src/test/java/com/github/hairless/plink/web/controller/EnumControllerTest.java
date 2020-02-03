package com.github.hairless.plink.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author: silence
 * @date: 2020/2/3
 */
@Slf4j
class EnumControllerTest extends BaseControllerTest {

    @Test
    void jobInstanceStatus() throws Exception {
        getTest("/mng/enum/jobInstanceStatus");
    }

    @Test
    void jobType() throws Exception {
        getTest("/mng/enum/jobType");
    }
}