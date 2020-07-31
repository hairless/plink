package com.github.hairless.plink.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.model.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

/**
 * @author: silence
 * @date: 2020/1/15
 */
@Slf4j
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/api-docs")
public class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    protected String get(String url) throws Exception {
        return getMvcResult(url).getResponse().getContentAsString();
    }

    protected MvcResult getMvcResult(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)).andDo(document("get")).andReturn();
    }

    protected void getTest(String url) throws Exception {
        log.info("get url: {}", url);
        MvcResult mvcResult = getMvcResult(url);
        String respBody = mvcResult.getResponse().getContentAsString();
        log.info("response: {}", respBody);
        assert mvcResult.getResponse().getStatus() == 200;
        assert JSON.parseObject(respBody, Result.class).getSuccess();
    }

    protected String post(String url, Object param) throws Exception {
        return postMvcResult(url, param).getResponse().getContentAsString();
    }

    protected MvcResult postMvcResult(String url, Object param) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url).
                characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).
                content(JSONObject.toJSONString(param))).andDo(document("post")).andReturn();
    }

    protected void postTest(String url, Object param) throws Exception {
        log.info("post url: {} request: {}", url, param);
        MvcResult mvcResult = postMvcResult(url, param);
        String respBody = mvcResult.getResponse().getContentAsString();
        log.info("response: {}", respBody);
        assert mvcResult.getResponse().getStatus() == 200;
        assert JSON.parseObject(respBody, Result.class).getSuccess();
    }
}
