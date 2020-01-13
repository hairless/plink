package com.github.hairless.plink.model.enums;

import lombok.Getter;

/**
 * @author silence
 * @date 2020/01/13
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
