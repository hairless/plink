package com.github.hairless.plink.web.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author: silence
 * @date: 2020/1/17
 */
@Slf4j
class JobControllerTest extends BaseControllerTest {

    @Test
    void addJob() throws Exception {
        JSONObject param = new JSONObject();
        param.put("name", "test_add_job");
        param.put("type", "1");
        postTest("/mng/job/addJob", param);
    }

    @Test
    void deleteJobs() throws Exception {
    }

    @Test
    void deleteJob() {
    }

    @Test
    void updateJob() throws Exception {
        JSONObject param = new JSONObject();
        param.put("name", "test_update_job");
        param.put("type", "1");
        postTest("/mng/job/updateJob", param);
    }

    @Test
    void queryJob() throws Exception {
        getTest("/mng/job/queryJob/1");
    }

    @Test
    void queryJobs() throws Exception {
        getTest("/mng/job/queryJobs");
    }

    @Test
    void uploadJar() {
    }

    @Test
    void jarList() {
    }
}