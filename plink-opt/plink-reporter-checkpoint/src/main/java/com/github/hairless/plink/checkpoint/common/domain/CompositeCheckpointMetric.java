package com.github.hairless.plink.checkpoint.common.domain;

import com.github.hairless.plink.common.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author lipan
 * @date 2021-01-24
 */
public class CompositeCheckpointMetric extends CompositeMetric {

    private static final long serialVersionUID = 1L;
    public static final Type KEY = Type.CHECKPOINT;
    private Long instanceId;

    public CompositeCheckpointMetric(Long jobId, String metricValue, Long reportTimestamp, Long instanceId) {
        super(jobId, KEY.getVal(), metricValue, reportTimestamp);
        this.instanceId = instanceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeCheckpointMetric that = (CompositeCheckpointMetric) o;
        String thisV = this.getMetricValue();
        String thatV = that.getMetricValue();
        if (StringUtils.isBlank(thisV) || StringUtils.isBlank(thatV))
            return false;
        Checkpoint thisCk = JsonUtil.parseObject(thisV, Checkpoint.class);
        Checkpoint thatCk = JsonUtil.parseObject(thatV, Checkpoint.class);
        if (thisCk == null || thatCk == null)
            return false;
        return Objects.equals(instanceId, that.instanceId)
                && Objects.equals(this.getJobId(), that.getJobId())
                && Objects.equals(thisCk.getLastCheckpointExternalPath(), thatCk.getLastCheckpointExternalPath());
    }

    @Override
    public int hashCode() {
        String metricValue = this.getMetricValue();
        Checkpoint ck = JsonUtil.parseObject(metricValue, Checkpoint.class);
        return Objects.hash(instanceId, this.getJobId(), ck.getLastCheckpointExternalPath());
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Checkpoint implements Serializable {
        private static final long serialVersionUID = 1L;
        private long lastCheckpointDuration;
        private long lastCheckpointSize;
        private String lastCheckpointExternalPath;
    }
}
