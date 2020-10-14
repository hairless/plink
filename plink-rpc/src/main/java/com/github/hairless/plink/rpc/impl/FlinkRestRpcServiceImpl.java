package com.github.hairless.plink.rpc.impl;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.common.util.FlinkConfigUtil;
import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import com.github.hairless.plink.rpc.FlinkRestRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author: silence
 * @date: 2020/1/27
 */
@Slf4j
@Service
public class FlinkRestRpcServiceImpl implements FlinkRestRpcService {
    private static final String VERSION = "/v1";
    private static final String JOBS = VERSION + "/jobs";
    private static final String JOBS_JOBId = JOBS + "/%s";

    //flink ui address
    private static final String JOB_UI_ADDRESS = "/#/job/%s";

    @Override
    public String queryJobStatus(String jobId) {
        HttpGet httpGet = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            httpGet = new HttpGet(String.format(FlinkConfigUtil.getRestAddress() + JOBS_JOBId, jobId));
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            String resJson = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
            return JSON.parseObject(resJson).getString("state");
        } catch (Exception e) {
            throw new PlinkRuntimeException("queryJobStatus error", e);
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    @Override
    public void stopJob(String jobId) {
        HttpPatch httpPatch = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            httpPatch = new HttpPatch(String.format(FlinkConfigUtil.getRestAddress() + JOBS_JOBId, jobId));
            CloseableHttpResponse httpResponse = httpClient.execute(httpPatch);
            String resJson = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
            String errors = JSON.parseObject(resJson).getString("errors");
            if (errors != null) {
                throw new PlinkRuntimeException("stopJob error:" + errors);
            }
        } catch (Exception e) {
            throw new PlinkRuntimeException("stopJob error", e);
        } finally {
            if (httpPatch != null) {
                httpPatch.releaseConnection();
            }
        }
    }

    @Override
    public String getJobUiAddress(String jobId) {
        try {
            return String.format(FlinkConfigUtil.getRestAddress() + JOB_UI_ADDRESS, jobId);
        } catch (Exception e) {
            throw new PlinkRuntimeException("getJobUiAddress error", e);
        }
    }


}
