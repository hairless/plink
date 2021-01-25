package com.github.hairless.plink.checkpoint.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lipan
 * @date 2021-01-24
 */
@Setter
@Getter
@NoArgsConstructor
public class CompositeMetric extends FlinkStreamingInfo{

    private static final long serialVersionUID = 1L;

    public CompositeMetric(Long jobId, String metricName, String metricValue, Long reportTimestamp) {
        super(jobId, metricName, metricValue, reportTimestamp);
    }
}
