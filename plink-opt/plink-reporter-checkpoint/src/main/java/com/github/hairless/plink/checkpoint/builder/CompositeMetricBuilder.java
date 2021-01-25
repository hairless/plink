package com.github.hairless.plink.checkpoint.builder;

import com.github.hairless.plink.checkpoint.common.domain.CompositeCheckpointMetric;
import com.github.hairless.plink.checkpoint.common.domain.FlinkStreamingInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.flink.metrics.Metric;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lipan
 * @date 2021-01-24
 */
public interface CompositeMetricBuilder<T extends Metric> {
    List<FlinkStreamingInfo> build(Map<T, String> flinkMetricMap, BuildConfig buildConfig);

    Map<FlinkStreamingInfo.Type, CompositeMetricBuilder> builderMap = new ConcurrentHashMap<>();

    static void init() {
        builderMap.put(CompositeCheckpointMetric.KEY, new CompositeCheckpointMetricBuilder());
    }

    static CompositeMetricBuilder builder(FlinkStreamingInfo.Type key) {
        if (builderMap.isEmpty()) {
            init();
        }
        return builderMap.get(key);
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class BuildConfig {
        private String jobId;
        private String instanceId;
        private String delimiter;
        private long currentReportTime;
    }
}
