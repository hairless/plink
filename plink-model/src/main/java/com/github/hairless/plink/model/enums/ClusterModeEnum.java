package com.github.hairless.plink.model.enums;

import lombok.Getter;

/**
 * @description:
 * @author: lxs
 * @create: 2021-01-27 14:00
 */
@Getter
public enum ClusterModeEnum {
    STANDALONE(1,"standalone"),
    YARN(1,"yarn");

    private Integer value;
    private String desc;

    ClusterModeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
