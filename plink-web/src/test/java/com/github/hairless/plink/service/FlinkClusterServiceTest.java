package com.github.hairless.plink.service;

import com.github.hairless.plink.BaseTest;
import com.github.hairless.plink.model.common.FlinkConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author aven danxieai@163.com
 * @version 0.1
 * @date 2020/1/24
 */
public class FlinkClusterServiceTest extends BaseTest {

    @Autowired
    FlinkClusterService flinkClusterService;


    @Test
    public void submitJob() {
        System.setProperty("user.dir", "/Users/wxy/Develop");
        System.out.println(flinkClusterService.uploadJar("wordcount.jar"));
    }


    @Test
    public void runJob() {
        String jarId = "342568f0-94fb-4646-9f0c-d79f5d4bb531_wordcount.jar";
        FlinkConfig flinkConfig = new FlinkConfig();
        flinkConfig.setJarName("wordcount.jar");
        System.out.println(flinkClusterService.runJob(jarId, flinkConfig));
    }

    @Test
    public void jobState() {
        String job = "6d33604e0d5c603b66be5a7eb964385c";
        System.out.println(flinkClusterService.jobDetail(job));
    }

    @Test
    public void cancelJob() {
        String jobId = "7e6ba223d4f38f3892ef4112383bd581";
        flinkClusterService.cancelJob(jobId);
    }


}
