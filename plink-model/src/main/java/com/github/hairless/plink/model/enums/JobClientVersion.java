package com.github.hairless.plink.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author: silence
 * @date: 2020/2/13
 * {@link com.github.hairless.plink.model.pojo.Job#clientVersion}
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JobClientVersion {
    V_1_9_X("1.9.x", "flink 1.9"),
    V_1_10_X("1.10.x", "flink 1.10");

    private String value;
    private String desc;

    JobClientVersion(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
