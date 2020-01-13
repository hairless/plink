package com.github.hairless.plink.model.enums;

import lombok.Getter;

/**
 * 任务类型枚举
 *
 * @author silence
 * @date 2020/01/13
 * {@link com.github.hairless.plink.model.pojo.Job#type}
 */
@Getter
public enum JobTypeEnum {
    CUSTOM(1,"自定义任务");

    private Integer value;
    private String desc;

    JobTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
