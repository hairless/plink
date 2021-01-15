package com.github.hairless.plink.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author chaixiaoxue
 * @date 2021/1/14 17:45
 * {@link com.github.hairless.plink.model.pojo.JobStateInfo#type}
 */

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JobStateInfoTypeEnum {

    CHECKPOINT(0,"checkpoint"),
    SAVEPOINT(1,"savepoint");

    private Integer code;
    private String desc;

    JobStateInfoTypeEnum (Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public static JobStateInfoTypeEnum getEnum (Integer code){
        if (code == null){
            return null;
        }
        for (JobStateInfoTypeEnum jobStateInfoTypeEnum : JobStateInfoTypeEnum.values()) {
            if (jobStateInfoTypeEnum.getCode().equals(code)){
                return jobStateInfoTypeEnum;
            }
        }
        return null;
    }

}
