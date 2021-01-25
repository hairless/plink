package com.github.hairless.plink.checkpoint.common.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author lipan
 * @date 2021-01-24
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FlinkStreamingInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long jobId;
    private String metricName;
    private String metricValue;
    private Long reportTimestamp;

    @Getter
    public enum Type {
        LAST_CHECKPOINT_DURATION("lastCheckpointDuration", null),
        LAST_CHECKPOINT_SIZE("lastCheckpointSize", null),
        LAST_CHECKPOINT_EXTERNALPATH("lastCheckpointExternalPath", null),
        CHECKPOINT("checkpoint", Arrays.asList(Type.LAST_CHECKPOINT_DURATION.getVal(), Type.LAST_CHECKPOINT_SIZE.getVal(), Type.LAST_CHECKPOINT_EXTERNALPATH.getVal()));

        private String val;
        private List<String> suffixes;

        Type(String val, List<String> suffixes) {
            this.val = val;
            this.suffixes = suffixes;
        }
    }
}
