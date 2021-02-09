package com.github.hairless.plink.checkpoint.reporter;

import com.github.hairless.plink.checkpoint.reporter.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.MetricConfig;
import org.apache.flink.metrics.reporter.InstantiateViaFactory;
import org.apache.flink.metrics.reporter.Scheduled;

import java.util.HashMap;
import java.util.Map;

import static com.github.hairless.plink.checkpoint.reporter.PlinkCheckpointReporterOptions.*;

/**
 * @description: Remote Reporter
 * @author: thorntree
 * @create: 2021-01-26 15:44
 */
@Slf4j
@InstantiateViaFactory(factoryClassName = "com.github.hairless.plink.checkpoint.reporter.PlinkCheckpointReporterFactory")
public class PlinkCheckpointReporter extends AbstractReporter implements Scheduled {

    public static final String FILED_JOB_ID = "jobId";
    public static final String FILED_JOB_INSTANCE_ID = "instanceId";
    public static final String FILED_EXTERNAL_PATH = "externalPath";
    public static final String FILED_DURATION = "duration";
    public static final String FILED_SIZE = "size";
    public static final String FILED_TYPE = "type";
    public static final String FILED_REPORT_TIMESTAMP = "reportTimestamp";

    public static final String METRIC_CHECKPOINT_NONE = "-1";
    public static final String METRIC_CHECKPOINT_NULL = "n/a";

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
        Map<String,Map<String,Object>> flinkJobIdMetric = new HashMap<>();
        try {
            for (Map.Entry<Gauge<?>, String> metric : gauges.entrySet()) {
                putMetricToMap(metric,Constants.METRIC_EXTERNAL_PATH,FILED_EXTERNAL_PATH,flinkJobIdMetric);
                putMetricToMap(metric,Constants.METRIC_DURATION,FILED_DURATION,flinkJobIdMetric);
                putMetricToMap(metric,Constants.METRIC_SIZE,FILED_SIZE,flinkJobIdMetric);
            }
            for(Map.Entry<String,Map<String,Object>> map:flinkJobIdMetric.entrySet()){
                HttpUtil.doPost(remoteService,map.getValue());
            }
        }catch (Exception e){
            log.error("Report info error. ",e);
            throw new RuntimeException("Report info error. ", e);
        }
    }

    /**
     * Put Metric to Map
     * @param metric
     * @param metricName
     * @param filed
     * @param flinkJobIdMetric
     */
    private void putMetricToMap(Map.Entry<Gauge<?>, String> metric,String metricName,String filed,Map<String,Map<String,Object>> flinkJobIdMetric){
        if(metric.getValue().contains(metricName)
            && !METRIC_CHECKPOINT_NONE.equals(String.valueOf(metric.getKey().getValue()))
            && !METRIC_CHECKPOINT_NULL.equals(String.valueOf(metric.getKey().getValue()))){

            String[] valueArray = metric.getValue().split("_");
            if(valueArray.length<2){
                return;
            }
            String flinkJobId = valueArray[valueArray.length-1];
            if("NULL".equals(flinkJobId)){
                return;
            }
            if(flinkJobIdMetric.containsKey(flinkJobId)){
                flinkJobIdMetric.get(flinkJobId).put(filed,metric.getKey().getValue());
            }else{
                Map<String,Object> map = new HashMap<>();
                map.put(FILED_TYPE,0);
                map.put(FILED_JOB_ID,plinkJobId);
                map.put(FILED_JOB_INSTANCE_ID,plinkInstanceId);
                map.put(FILED_REPORT_TIMESTAMP,System.currentTimeMillis()/1000);
                map.put(filed,metric.getKey().getValue());
                flinkJobIdMetric.put(flinkJobId,map);
            }
        }
    }
}
