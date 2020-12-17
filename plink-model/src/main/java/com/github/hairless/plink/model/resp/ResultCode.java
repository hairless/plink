package com.github.hairless.plink.model.resp;

import lombok.Getter;

/**
 * @author: silence
 * @date: 2020/1/14
 */
@Getter
public enum ResultCode {
    SUCCESS(10001, true, "success"),
    FAILURE(10002, false, "failure"),
    EXCEPTION(10003, false, "exception"),
    EXCEPTION_DATA(10004, true, "exception with data");

    private Integer code;
    private Boolean success;
    private String desc;

    ResultCode(Integer code, Boolean success, String desc) {
        this.code = code;
        this.success = success;
        this.desc = desc;
    }
}
