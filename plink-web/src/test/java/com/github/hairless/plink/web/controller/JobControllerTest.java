package com.github.hairless.plink.web.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author: silence
 * @date: 2020/1/17
 */
@Slf4j
public class JobControllerTest extends BaseControllerTest {

    @Test
    public void addJob() throws Exception {
        JSONObject param = new JSONObject();
        param.put("name", "test_add_job");
        param.put("type", "1");
        //postTest("/mng/job/addJob", param);
    }

    @Test
    public void deleteJobs() throws Exception {
    }

    @Test
    public void deleteJob() {
    }

    @Test
    public void updateJob() throws Exception {
        JSONObject param = new JSONObject();
        param.put("name", "test_update_job");
        param.put("type", "1");
        //postTest("/mng/job/updateJob", param);
    }

    @Test
    public void queryJob() throws Exception {
        getTest("/mng/job/queryJob/1");
    }

    @Test
    public void queryJobs() throws Exception {
        getTest("/mng/job/queryJobs");
    }

    @Test
    public void uploadJar() {
    }

    @Test
    public void jarList() {
    }
}