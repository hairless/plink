package com.github.hairless.plink.service.exception;

/**
 * @author: silence
 * @date: 2020/1/19
 */
public class PlinkException extends Exception {
    private static final long serialVersionUID = -1L;

    public PlinkException() {
    }

    public PlinkException(String message) {
        super(message);
    }

    public PlinkException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlinkException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public PlinkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
