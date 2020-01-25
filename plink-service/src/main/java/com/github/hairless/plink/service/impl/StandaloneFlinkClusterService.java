package com.github.hairless.plink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.resp.JobDetail;
import com.github.hairless.plink.service.FlinkClusterService;
import com.github.hairless.plink.service.exception.FlinkClusterServiceException;
import com.github.hairless.plink.service.tools.FlinkConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/1/19
 */
@Component("standaloneFlinkClusterService")
@Slf4j
public class StandaloneFlinkClusterService implements FlinkClusterService {

    @Autowired
    private FlinkConfigFactory flinkConfigFactory;

    @Override
    public String runJob(String jarId, FlinkConfig config){
        String queryParam = JSONObject.toJSONString(copyConfig(config));
        HttpPost httpPost = new HttpPost(flinkBaseUrl() + String.format(FlinkApiConstants.jars_run, jarId));
        HttpEntity httpEntity = new StringEntity(queryParam, ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpResponse response = httpclient.execute(httpPost);
            JSONObject flinkRestRes = JSON.parseObject(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            return flinkRestRes.getString("jobid");
        } catch (IOException e) {
            throw new FlinkClusterServiceException(e);
        }

    }

    @Override
    public String uploadJar(String jarfile) {
        //本地jar包存放路径
        String parentDir = System.getProperty("user.dir");
        String jarPath = parentDir + "/uploadJars/" + jarfile;
        //向flink平台提交jar
        HttpPost httpPost = new HttpPost(flinkBaseUrl() + FlinkApiConstants.jars_upload);
        //httpPost.setHeader("Content-Type", "application/x-java-archive");
        try {
            try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                File jarFile = new File(jarPath);
                builder.addBinaryBody(
                        "jarfile",
                        new FileInputStream(jarFile),
                        ContentType.create("application/x-java-archive"),
                        jarFile.getName()
                );
                httpPost.setEntity(builder.build());
                HttpResponse response = httpclient.execute(httpPost);
                JSONObject flinkRestRes = JSON.parseObject(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
                log.debug("upload jar response {}", flinkRestRes.toJSONString());
                if (!"success".equals(flinkRestRes.getString("status"))) {
                    throw new FlinkClusterServiceException("upload jar to cluster fail");
                }
                String filename = flinkRestRes.getString("filename");
                String filenames[] = filename.split("/");
                //&#x9884;&#x7559;&#x5411;mysql&#x5199;&#x8868; id
                return filenames[filenames.length - 1];
            }
        } catch (IOException e) {
            throw new FlinkClusterServiceException(e);
        }
    }

    private Map<String, Object> copyConfig(FlinkConfig config) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(config);
        String[] confArray = new String[]{
                "entryClass", "programArgs", "programArgsList", "parallelism",
                "savepointPath", "allowNonRestoredState"
        };
        Map<String, Object> confMap = new HashMap<>();
        for (String element : confArray) {
            if (jsonObject.containsKey(element)) {
                confMap.put(element, jsonObject.remove(element));
            }
        }
        return confMap;
    }

    private String flinkBaseUrl() {
        return "http://" + flinkConfigFactory.flinkClusterHost()
                + ":" + flinkConfigFactory.flinkClusterPort();
    }

    @Override
    public JobDetail jobDetail(String jobId) {
        HttpGet get = new HttpGet(flinkBaseUrl() + String.format(FlinkApiConstants.jobs_state, jobId));
        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpResponse response = httpclient.execute(get);
            JSONObject flinkRestRes = JSON.parseObject(EntityUtils.toString(response.getEntity(),
                    StandardCharsets.UTF_8));
            log.debug("query job {} state , {}", jobId, flinkRestRes.toJSONString());
            return flinkRestRes.toJavaObject(JobDetail.class);
        } catch (IOException e) {
            throw new FlinkClusterServiceException(e);
        }
    }

    @Override
    public void cancelJob(String jobId) throws FlinkClusterServiceException{
        HttpPatch patch = new HttpPatch(flinkBaseUrl() +
                String.format(FlinkApiConstants.jobs_state, jobId));
        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            httpclient.execute(patch);
        } catch (IOException e) {
            throw new FlinkClusterServiceException(
                    String.format("cannot terminates job ID %s case by \n", jobId), e);
        }
    }
}
