package com.github.hairless.plink.model.exception;

/**
 * @author: silence
 * @date: 2020/2/17
 */
public class PlinkException extends Exception {
    private static final long serialVersionUID = -1L;

    public PlinkException() {
    }

    public PlinkException(String message) {
        super(message);
    }

    public PlinkException(String message, Throwable cause) {
        super(message + ":" + cause.getMessage(), cause);
    }

    public PlinkException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}