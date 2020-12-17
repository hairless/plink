package com.github.hairless.plink.model.exception;

/**
 * @author: silence
 * @date: 2020/12/16
 */
public class PlinkDataException extends PlinkMessageException {
    private static final long serialVersionUID = -1L;

    private Object data;

    public PlinkDataException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

}
