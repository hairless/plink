package com.github.hairless.plink.metrics.checkpoint.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Map;

/**
 * @description: Json Utils
 * @author: thorntree
 * @create: 2021-01-26 15:46
 */
@Slf4j
public class HttpUtil {

    /**
     * Do Http Post
     * @param url
     * @return
     */
    public static Boolean doPost(String url,Map<String,Object> map) {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(getConfig());
        String json = JsonUtil.toJSONString(map);
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode()==200){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            log.error("Remote service error.",e);
            return false;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Request Config
     * @return
     */
    private static RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(10000)
                .build();
        return config;
    }
}
