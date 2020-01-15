package com.github.hairless.plink;

import com.alibaba.fastjson.JSONObject;
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
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/api-docs")
public class BaseControllerTest extends BaseTest {
    @Autowired
    protected MockMvc mockMvc;

    protected String get(String url) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.get(url)).andDo(document("get")).andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected MvcResult getMvcResult(String url) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.get(url)).andDo(document("get")).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected String post(String url, Object param) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.post(url).
                    characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).
                    content(JSONObject.toJSONString(param))).andDo(document("post")).andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected MvcResult postMvcResult(String url, Object param) {
        try {
            return mockMvc.perform(MockMvcRequestBuilders.post(url).
                    characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).
                    content(JSONObject.toJSONString(param))).andDo(document("post")).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
