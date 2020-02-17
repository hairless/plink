package com.github.hairless.plink.model.exception;

/**
 * @author: silence
 * @date: 2020/1/19
 */
public class PlinkRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public PlinkRuntimeException() {
        super();
    }

    public PlinkRuntimeException(String message) {
        super(message);
    }

    public PlinkRuntimeException(String message, Throwable cause) {
        super(message + ":" + cause.getMessage(), cause);
    }

    public PlinkRuntimeException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
