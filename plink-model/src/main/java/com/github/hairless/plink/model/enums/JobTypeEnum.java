package com.github.hairless.plink.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * 作业类型枚举
 *
 * @author silence
 * @date 2020/01/13
 * {@link com.github.hairless.plink.model.pojo.Job#type}
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JobTypeEnum {
    FLINK_JAR(1, "FLINK JAR", true),
    FLINK_PYTHON(2, "FLINK PYTHON", false),
    FLINK_SQL(3, "FLINK SQL", true);

    private Integer value;
    private String desc;
    private Boolean enable;

    JobTypeEnum(Integer value, String desc, Boolean enable) {
        this.value = value;
        this.desc = desc;
        this.enable = enable;
    }

    public static JobTypeEnum getEnum(Integer value) {
        if (value == null)
            return null;
        for (JobTypeEnum jobTypeEnum : JobTypeEnum.values()) {
            if (jobTypeEnum.getValue().equals(value))
                return jobTypeEnum;
        }
        return null;
    }
}
