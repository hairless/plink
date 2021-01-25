package com.github.hairless.plink.checkpoint.reporter;

import com.github.hairless.plink.checkpoint.agent.AgentLocation;
import com.github.hairless.plink.checkpoint.builder.CompositeMetricBuilder;
import com.github.hairless.plink.checkpoint.common.domain.CompositeCheckpointMetric;
import com.github.hairless.plink.checkpoint.common.domain.FlinkStreamingInfo;
import com.github.hairless.plink.checkpoint.container.ConfigurationHolder;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.metrics.MetricConfig;
import org.apache.flink.metrics.reporter.AbstractReporter;
import org.apache.flink.metrics.reporter.Scheduled;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lipan
 * @date 2021-01-24
 */
public class MySQLReporter extends AbstractReporter implements Scheduled {

    private String jobId;
    private String instanceId;
    private String delimiter;

    @Override
    public String filterCharacters(String s) {
        return s;
    }


    @Override
    public void report() {
        CompositeMetricBuilder.BuildConfig buildConfig = new CompositeMetricBuilder.BuildConfig(this.jobId, this.instanceId, this.delimiter, Instant.now().toEpochMilli());
        List<FlinkStreamingInfo> infos = new ArrayList<>();
        AgentLocation location = ConfigurationHolder.getLocation();
        if(location.equals(AgentLocation.JOBMANAGER)){
            infos.addAll(CompositeMetricBuilder.builder(CompositeCheckpointMetric.KEY).build(gauges, buildConfig));
        }
        // 数据插入MySQL库表
        for (FlinkStreamingInfo info : infos) {

        }
    }

    @Override
    public void open(MetricConfig metricConfig) {
        this.delimiter = metricConfig.getString(ConfigConstants.METRICS_REPORTER_SCOPE_DELIMITER, ".");

    }

    @Override
    public void close() {
    }
}
