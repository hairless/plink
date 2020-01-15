package com.github.hairless.plink.model.pojo;


import com.github.hairless.plink.model.enums.ResultCode;
import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * @author: silence
 * @date: 2020/1/14
 */
@Getter
@Setter
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean success;
    private String code;
    private String msg;
    private T data;
    private String exceptionMessage;
    private String exceptionStackTrace;

    public Result(ResultCode resultCode) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.msg = resultCode.getDesc();
    }

    public Result(ResultCode resultCode, T data) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.msg = resultCode.getDesc();
        this.data = data;
    }

    public Result(ResultCode resultCode, String msg) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.msg = msg;
    }

    public Result(ResultCode resultCode, Exception exception) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.msg = resultCode.getDesc();
        if (exception != null) {
            this.exceptionMessage = exception.getMessage();
            StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));
            this.exceptionStackTrace = stringWriter.toString();
        }
    }

    public Result(ResultCode resultCode, String msg, Exception exception) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.msg = msg;
        if (exception != null) {
            this.exceptionMessage = exception.getMessage();
            StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));
            this.exceptionStackTrace = stringWriter.toString();
        }
    }
}
