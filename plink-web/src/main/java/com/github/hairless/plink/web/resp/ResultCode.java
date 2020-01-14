package com.github.hairless.plink.web.resp;

import lombok.Getter;

/**
 * @author: silence
 * @date: 2020/1/14
 */
@Getter
public enum ResultCode {
    SUCCESS("10001", true, "success"),
    FAILURE("10002", false, "failure"),
    EXCEPTION("10003", false, "exception");

    private String code;
    private Boolean success;
    private String desc;

    ResultCode(String code, Boolean success, String desc) {

        this.code = code;
        this.success = success;
        this.desc = desc;
    }
}
