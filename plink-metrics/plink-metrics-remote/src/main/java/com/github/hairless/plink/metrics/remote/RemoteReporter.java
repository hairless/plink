package com.github.hairless.plink.metrics.remote;

import com.github.hairless.plink.metrics.remote.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.MetricConfig;
import org.apache.flink.metrics.reporter.AbstractReporter;
import org.apache.flink.metrics.reporter.InstantiateViaFactory;
import org.apache.flink.metrics.reporter.Scheduled;

import java.util.HashMap;
import java.util.Map;

import static com.github.hairless.plink.metrics.remote.RemoteReporterOptions.*;

/**
 * @description: Remote Reporter
 * @author: thorntree
 * @create: 2021-01-26 15:44
 */
@Slf4j
@InstantiateViaFactory(factoryClassName = "com.github.hairless.plink.metrics.remote.RemoteReporterFactory")
public class RemoteReporter extends AbstractReporter implements Scheduled {


    public static final String METRICS_CHECKPOINT_EXTERNAL_PATH = "lastCheckpointExternalPath";
    public static final String METRICS_CHECKPOINT_DURATION = "lastCheckpointDuration";
    public static final String METRICS_CHECKPOINT_SIZE = "lastCheckpointSize";

    public static final String FILED_MODE = "mode";
    public static final String FILED_JOB_ID = "jobId";
    public static final String FILED_JOB_INSTANCE_ID = "instanceId";
    public static final String FILED_JOB_NAME = "jobName";
    public static final String FILED_EXTERNAL_PATH = "externalPath";
    public static final String FILED_DURATION = "duration";
    public static final String FILED_SIZE = "size";
    public static final String FILED_TYPE = "type";
    public static final String FILED_REPORT_TIMESTAMP = "reportTimestamp";

    public static final String DELIMITER = "\\.";

    public static final String METRICS_CHECKPOINT_NONE = "-1";
    public static final String METRICS_CHECKPOINT_NULL = "n/a";

    private String mode;
    private String remoteService;
    private String plinkJobId;
    private String plinkInstanceId;


    @Override
    public String filterCharacters(String s) {
        return s;
    }

    @Override
    public void open(MetricConfig metricConfig) {
        try {
            this.mode =  metricConfig.getString(MODE.key(),MODE.defaultValue());
            this.remoteService =  metricConfig.getString(SERVICE.key(),SERVICE.defaultValue());
            if(StringUtils.isBlank(this.remoteService)){
                throw new RuntimeException("Service is null. ");
            }
            this.plinkJobId =  metricConfig.getString(PLINK_JOB_ID.key(),PLINK_JOB_ID.defaultValue());
            this.plinkInstanceId =  metricConfig.getString(PLINK_INSTANCE_ID.key(),PLINK_INSTANCE_ID.defaultValue());
        } catch (Exception e) {
            log.error("Could not get service. ",e);
            throw new RuntimeException("Could not get service. ", e);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void report() {
        tryReport();
    }

    private void tryReport(){
        Map<String,Map<String,Object>> jobNameMetric = new HashMap<>();
        try {
            for (Map.Entry<Gauge<?>, String> metric : gauges.entrySet()) {
                putMetricToMap(metric,METRICS_CHECKPOINT_EXTERNAL_PATH,FILED_EXTERNAL_PATH,jobNameMetric);
                putMetricToMap(metric,METRICS_CHECKPOINT_DURATION,FILED_DURATION,jobNameMetric);
                putMetricToMap(metric,METRICS_CHECKPOINT_SIZE,FILED_SIZE,jobNameMetric);
            }
            for(Map.Entry<String,Map<String,Object>> map:jobNameMetric.entrySet()){
                HttpUtil.doPost(remoteService,map.getValue());
            }
        }catch (Exception e){
            log.error("report info error. ",e);
            throw new RuntimeException("report info error. ", e);
        }
    }

    /**
     * Put Metric to Map
     * @param metric
     * @param metricsFilter
     * @param filed
     * @param jobNameMetric
     */
    private void putMetricToMap(Map.Entry<Gauge<?>, String> metric,String metricsFilter,String filed,Map<String,Map<String,Object>> jobNameMetric){
        if(metric.getValue().contains(metricsFilter)
            && !METRICS_CHECKPOINT_NONE.equals(String.valueOf(metric.getKey().getValue()))
            && !METRICS_CHECKPOINT_NULL.equals(String.valueOf(metric.getKey().getValue()))){
            String[] valueArray = metric.getValue().split(DELIMITER);
            if(valueArray.length<4){
                return;
            }
            String jobName = valueArray[2];
            if(jobNameMetric.containsKey(jobName)){
                jobNameMetric.get(jobName).put(filed,metric.getKey().getValue());
            }else{
                Map<String,Object> map = new HashMap<>();
                map.put(FILED_MODE,mode);
                map.put(FILED_TYPE,0);
                map.put(FILED_JOB_ID,plinkJobId);
                map.put(FILED_JOB_INSTANCE_ID,plinkInstanceId);
                map.put(FILED_JOB_NAME,jobName);
                map.put(FILED_REPORT_TIMESTAMP,System.currentTimeMillis()/1000);
                map.put(filed,metric.getKey().getValue());
                jobNameMetric.put(jobName,map);
            }
        }
    }
}
