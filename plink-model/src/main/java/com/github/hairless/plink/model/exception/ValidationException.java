package com.github.hairless.plink.model.exception;

/**
 * @author: silence
 * @date: 2020/1/19
 */
public class ValidationException extends PlinkRuntimeException {
    private static final long serialVersionUID = -1L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
