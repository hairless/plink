package com.github.hairless.plink.common.conf;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.hairless.plink.model.common.UIOption;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.flink.runtime.jobgraph.tasks.CheckpointCoordinatorConfiguration;
import org.apache.flink.streaming.api.environment.ExecutionCheckpointingOptions;
import org.apache.flink.util.Preconditions;


/**
 * @author: silence
 * @date: 2021/1/12
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FlinkCommonSubmitOptions {

    CHECKPOINTING_INTERVAL(ExecutionCheckpointingOptions.CHECKPOINTING_INTERVAL.key(),
            UIOption.NUMBER.name("cp间隔")
                    .desc("checkPoint间隔，单位毫秒")
                    .validator(c -> Preconditions.checkState(
                            Long.parseLong(c) < CheckpointCoordinatorConfiguration.MINIMAL_CHECKPOINT_TIME,
                            "checkPoint间隔应该大于10ms"))),

    CHECKPOINTING_MODE(ExecutionCheckpointingOptions.CHECKPOINTING_MODE.key(),
            UIOption.RADIO.name("cp模式").desc("checkPoint模式，exactly-once or at-least-once")
                    .options(Lists.newArrayList("exactly-once", "at-least-once"))
                    .defaultValue("exactly-once")
                    .validator(c -> Preconditions.checkState(
                            Lists.newArrayList("exactly-once", "at-least-once").contains(c),
                            "checkPoint模式应该为exactly-once或at-least-once")));

    private final String key;
    private final UIOption uiOption;

    FlinkCommonSubmitOptions(String key, UIOption uiOption) {
        this.key = key;
        this.uiOption = uiOption;
    }
}
