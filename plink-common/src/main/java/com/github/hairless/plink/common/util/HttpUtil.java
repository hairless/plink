package com.github.hairless.plink.common.util;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.net.URI;
import java.util.Map;

/**
 * @author lipan
 * @date 2021-01-24
 */
public class HttpUtil {

    private Integer connectTimeout = 1000;
    private Integer socketTimeout = 1000;

    private RequestConfig defaultConfig() {
        return RequestConfig
                .custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
    }


    public String doGet(String url, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(defaultConfig());
            if (MapUtils.isNotEmpty(headers)) {
                for (Map.Entry<String, String> ele : headers.entrySet()) {
                    httpGet.addHeader(ele.getKey(), ele.getValue());
                }
            }
            if (MapUtils.isNotEmpty(params)) {
                URIBuilder uriBuilder = new URIBuilder(url);
                for (String key : params.keySet()) {
                    uriBuilder.addParameter(key, params.get(key));
                }
                url = uriBuilder.build().toString();
            }
            httpGet.setURI(URI.create(url));
            httpClient = HttpClients.custom().build();
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            throw new RuntimeException("doGet fail...");
        } finally {
            close(response, httpClient);
        }
        return null;
    }

    public String doPostJson(String url, String json) {
        return doPostJson(url, null, json);
    }

    public String doPostJson(String url, Map<String, String> headers, String json) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(defaultConfig());
            if (MapUtils.isNotEmpty(headers)) {
                headers.forEach(httpPost::addHeader);
            }
            if (json != null) {
                StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
                httpPost.setEntity(stringEntity);
            }
            httpClient = HttpClients.custom().build();
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            throw new RuntimeException("doPostJson fail...");
        } finally {
            close(response, httpClient);
        }
        return null;
    }

    public void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    throw new RuntimeException("close fail...");
                }
            }
        }
    }
}
