package com.github.hairless.plink.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * 作业实例状态枚举
 *
 * @author silence
 * @date 2020/01/13
 * {@link com.github.hairless.plink.model.pojo.JobInstance#status}
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JobInstanceStatusEnum {
    WAITING_START(0, "待启动", false),
    STARTING(1, "启动中", false),
    RUNNING(2, "运行中", false),
    START_FAILED(3, "启动失败", true),
    RUN_FAILED(4, "运行失败", true),
    STOPPED(5, "已停止", true),
    SUCCESS(6, "运行成功", true),
    UNKNOWN(-1, "未知", false);

    private Integer value;
    private String desc;
    private boolean FinalState;

    JobInstanceStatusEnum(Integer value, String desc, boolean finalState) {
        this.value = value;
        this.desc = desc;
        FinalState = finalState;
    }

    public static JobInstanceStatusEnum getEnum(Integer value) {
        if (value == null)
            return null;
        for (JobInstanceStatusEnum jobInstanceStatusEnum : JobInstanceStatusEnum.values()) {
            if (jobInstanceStatusEnum.getValue().equals(value))
                return jobInstanceStatusEnum;
        }
        return null;
    }
}
