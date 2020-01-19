package com.github.hairless.plink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.common.HttpUtil;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.exception.PlinkException;
import com.github.hairless.plink.model.resp.JobResp;
import com.github.hairless.plink.service.FlinkClusterService;
import org.springframework.stereotype.Component;

/**
 * @author: silence
 * @date: 2020/1/19
 */
@Component("standaloneFlinkClusterService")
public class StandaloneFlinkClusterService implements FlinkClusterService {

    @Override
    public String submitJob(JobResp jobResp) throws Exception {
        //TODO 通过 FLINK_HOME配置文件加载
        String flinkURL = "http://localhost:8081";
        //本地jar包存放路径
        String parentDir = System.getProperty("user.dir");
        String jarPath = parentDir + "/uploadJars/" + jobResp.getConfig().getJarName();
        //向flink平台提交jar
        String resJson = null;
        try {
            resJson = HttpUtil.sendFlinkJar(flinkURL, jarPath);
        } catch (Exception e) {
            throw new PlinkException("upload jar to cluster fail", e);
        }
        JSONObject flinkRestRes = JSON.parseObject(resJson);
        if (!"success".equals(flinkRestRes.getString("status"))) {
            throw new PlinkException("upload jar to cluster fail");
        }
        String filename = flinkRestRes.getString("filename");
        String filenames[] = filename.split("/");
        //&#x9884;&#x7559;&#x5411;mysql&#x5199;&#x8868; id
        String appId = filenames[filenames.length - 1];

        return appId;
    }

    @Override
    public JobInstanceStatusEnum jobStatus(JobResp jobResp) throws Exception {
        return null;
    }

    @Override
    public Boolean cancelJob(JobResp jobResp) throws Exception {
        return null;
    }
}
