package com.github.hairless.plink.model.enums;

import lombok.Getter;

/**
 * 作业实例状态枚举
 *
 * @author silence
 * @date 2020/01/13
 * {@link com.github.hairless.plink.model.pojo.JobInstance#status}
 */
@Getter
public enum JobInstanceStatusEnum {
    WAITING_START(0, "待启动"),
    STARTING(1, "启动中"),
    RUNNING(2, "运行中"),
    START_FAILED(3, "启动失败"),
    RUN_FAILED(4, "运行失败"),
    STOPPED(5, "已停止"),
    SUCCESS(6, "运行成功"),
    UNKNOWN(-1, "未知");

    private Integer value;
    private String desc;

    JobInstanceStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
