package com.github.hairless.plink;

import com.github.hairless.plink.dao.mapper.PlinkTestMapper;
import com.github.hairless.plink.model.pojo.PlinkTest;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author: silence
 * @date: 2020/1/14
 */
@Slf4j
public class DemoTest extends BaseTest {

    @Autowired
    private PlinkTestMapper plinkTestMapper;

    @Test
    public void demoTest() {
        List<PlinkTest> plinkTestList = plinkTestMapper.selectAll();
        log.info(plinkTestList.toString());
    }

    @Test
    public void upload(){
        String jobId  = "2321";
        String parentDir = System.getProperty("user.dir");
        try {
            File uploadPath = new File(parentDir + "/uploadJars/" + jobId);
            if (uploadPath.exists()) {
                String[] fileNames = uploadPath.list();
                log.error(parentDir);

            }

        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {

        String parentDir = System.getProperty("user.dir");
        System.out.println(parentDir);
    }
}
