package com.github.hairless.plink.checkpoint.builder;

import com.github.hairless.plink.checkpoint.common.domain.CompositeCheckpointMetric;
import com.github.hairless.plink.checkpoint.common.domain.FlinkStreamingInfo;
import com.github.hairless.plink.common.util.JsonUtil;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lipan
 * @date 2021-01-24
 */
public class CompositeCheckpointMetricBuilder implements CompositeMetricBuilder<Gauge> {
    @Override
    public List<FlinkStreamingInfo> build(Map<Gauge, String> flinkMetricMap, BuildConfig buildConfig) {

        List<FlinkStreamingInfo> infos = new ArrayList<>();
        if (CollectionUtil.isNullOrEmpty(flinkMetricMap)) {
            return infos;
        }
        String jobId = buildConfig.getJobId();
        String instanceId = buildConfig.getInstanceId();
        String delimiter = buildConfig.getDelimiter();
        long currentReportTime = buildConfig.getCurrentReportTime();

        try {
            Map<String, Gauge> metrics = flinkMetricMap.entrySet().stream().filter(e -> {
                for (String suffix : CompositeCheckpointMetric.KEY.getSuffixes()) {
                    if (e.getValue().endsWith(suffix))
                        return true;
                }
                return false;
            }).collect(Collectors.toMap(e -> {
                String metricName = e.getValue();
                return metricName.split(delimiter)[4];
            }, Map.Entry::getKey));
            CompositeCheckpointMetric.Checkpoint ck = new CompositeCheckpointMetric.Checkpoint();
            for (Map.Entry<String, Gauge> e : metrics.entrySet()) {
                String metricName = e.getKey();
                String value = e.getValue().getValue().toString();
                if (metricName.equals(FlinkStreamingInfo.Type.LAST_CHECKPOINT_DURATION.getVal())) {
                    ck.setLastCheckpointDuration(Long.parseLong(value));
                } else if (metricName.equals(FlinkStreamingInfo.Type.LAST_CHECKPOINT_SIZE.getVal())) {
                    ck.setLastCheckpointSize(Long.parseLong(value));
                } else if (metricName.equals(FlinkStreamingInfo.Type.LAST_CHECKPOINT_EXTERNALPATH.getVal())) {
                    ck.setLastCheckpointExternalPath(value);
                }
            }
            CompositeCheckpointMetric metric = new CompositeCheckpointMetric(Long.parseLong(jobId), JsonUtil.toJSONString(ck), currentReportTime, Long.parseLong(instanceId));
            infos.add(metric);
        } catch (Exception e) {
            System.err.println("build fail..." + e.getMessage());
        }
        return infos;
    }
}
