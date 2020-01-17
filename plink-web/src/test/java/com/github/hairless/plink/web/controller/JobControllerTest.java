package com.github.hairless.plink.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.model.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

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
        MvcResult mvcResult = postMvcResult("/mng/job/addJob", param);
        String respBody = mvcResult.getResponse().getContentAsString();
        log.info(respBody);
        assert mvcResult.getResponse().getStatus() == 200;
        assert JSON.parseObject(respBody, Result.class).getSuccess();
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
        MvcResult mvcResult = postMvcResult("/mng/job/updateJob", param);
        String respBody = mvcResult.getResponse().getContentAsString();
        log.info(respBody);
        assert mvcResult.getResponse().getStatus() == 200;
        assert JSON.parseObject(respBody, Result.class).getSuccess();
    }

    @Test
    void queryJob() throws Exception {
        MvcResult mvcResult = getMvcResult("/mng/job/queryJob/1");
        String respBody = mvcResult.getResponse().getContentAsString();
        log.info(respBody);
        assert mvcResult.getResponse().getStatus() == 200;
        assert JSON.parseObject(respBody, Result.class).getSuccess();
    }

    @Test
    void queryJobs() throws Exception {
        MvcResult mvcResult = getMvcResult("/mng/job/queryJobs");
        String respBody = mvcResult.getResponse().getContentAsString();
        log.info(respBody);
        assert mvcResult.getResponse().getStatus() == 200;
        assert JSON.parseObject(respBody, Result.class).getSuccess();
    }

    @Test
    void uploadJar() {
    }

    @Test
    void jarList() {
    }
}