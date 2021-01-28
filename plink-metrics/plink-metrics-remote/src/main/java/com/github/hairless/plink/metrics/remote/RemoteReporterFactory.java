package com.github.hairless.plink.metrics.remote;

import org.apache.flink.metrics.reporter.InterceptInstantiationViaReflection;
import org.apache.flink.metrics.reporter.MetricReporter;
import org.apache.flink.metrics.reporter.MetricReporterFactory;

import java.util.Properties;


/**
 * @description: Remote Reporter
 * @author: thorntree
 * @create: 2021-01-26 15:48
 */
@InterceptInstantiationViaReflection(reporterClassName = "com.github.hairless.plink.metrics.remote.RemoteReporter")
public class RemoteReporterFactory implements MetricReporterFactory {

    @Override
    public MetricReporter createMetricReporter(Properties properties) {
        return new RemoteReporter();
    }
}
