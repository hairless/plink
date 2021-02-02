package com.github.hairless.plink.checkpoint.reporter;

import org.apache.flink.metrics.*;
import org.apache.flink.metrics.reporter.MetricReporter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author: liuxiaoshuai
 * @Date: 2021/2/2
 * @Description: Abstract Reporter
 */
public abstract class AbstractReporter implements MetricReporter, CharacterFilter {

    public static final String METRICS_EXTERNAL_PATH = "lastCheckpointExternalPath";
    public static final String METRICS_DURATION = "lastCheckpointDuration";
    public static final String METRICS_SIZE = "lastCheckpointSize";
    public static final String METRICS_JOB_ID = "job_id";


    private static final Pattern UNALLOWED_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9:_]");
    private static final CharacterFilter CHARACTER_FILTER =
            new CharacterFilter() {
                @Override
                public String filterCharacters(String input) {
                    return replaceInvalidChars(input);
                }
            };

    private static final char SCOPE_SEPARATOR = '_';

    static String replaceInvalidChars(final String input) {
        return UNALLOWED_CHAR_PATTERN.matcher(input).replaceAll("_");
    }

    private CharacterFilter labelValueCharactersFilter = CHARACTER_FILTER;

    protected final Map<Gauge<?>, String> gauges = new HashMap<>();

    @Override
    public void notifyOfAddedMetric(Metric metric, String metricName, MetricGroup group) {
        synchronized (this) {
            if (metric instanceof Gauge
                    && (METRICS_EXTERNAL_PATH.equals(metricName)
                    || METRICS_DURATION.equals(metricName)
                    || METRICS_SIZE.equals(metricName))) {
                String job_id = getFlinkJobId(group);
                final String name = group.getMetricIdentifier(metricName, this);
                String lastName = name+SCOPE_SEPARATOR+job_id;
                gauges.put((Gauge<?>) metric, lastName);
            }
        }
    }


    @Override
    public void notifyOfRemovedMetric(Metric metric, String metricName, MetricGroup group) {
        synchronized (this) {
            if (metric instanceof Gauge) {
                gauges.remove(metric);
            }
        }
    }

    /**
     * Get flink job id
     * @param group
     * @return
     */
    private String getFlinkJobId(MetricGroup group){
        for (final Map.Entry<String, String> dimension : group.getAllVariables().entrySet()) {
            final String key = dimension.getKey();
            if(METRICS_JOB_ID.equals(CHARACTER_FILTER.filterCharacters(key.substring(1, key.length() - 1)))){
                return labelValueCharactersFilter.filterCharacters(dimension.getValue());
            }
        }
        return "NULL";
    }

}
