package com.github.hairless.plink.web.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

/**
 * @author: silence
 * @date: 2020/1/14
 */
@Slf4j
public class DemoControllerTest extends BaseControllerTest {


    @Test
    public void demoTestGet() throws Exception {
        String result = get("/plinkTest?key1=val1");
        log.info(result);
    }

    @Test
    public void demoTestGet2() throws Exception {
        MvcResult mvcResult = getMvcResult("/plinkTest?key1=val1");
        assert mvcResult.getResponse().getStatus() == 200;
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void demoTestPost() throws Exception {
        JSONObject param = new JSONObject();
        param.put("key1", "val1");
        param.put("key2", "val2");
        String result = post("/plinkTest", param);
        log.info(result);
    }

    @Test
    public void demoTestPost2() throws Exception {
        JSONObject param = new JSONObject();
        param.put("key1", "val1");
        param.put("key2", "val2");
        MvcResult mvcResult = postMvcResult("/plinkTest", param);
        assert mvcResult.getResponse().getStatus() == 200;
        log.info(mvcResult.getResponse().getContentAsString());
    }
}
