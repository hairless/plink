package com.github.hairless.plink.service.exception;

/**
 * @author aven danxieai@163.com
 * @version 0.1
 * @date 2020/1/24
 */
public class FlinkClusterServiceException extends RuntimeException {
    public FlinkClusterServiceException() {
    }

    public FlinkClusterServiceException(String message) {
        super(message);
    }

    public FlinkClusterServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlinkClusterServiceException(Throwable cause) {
        super(cause);
    }

    public FlinkClusterServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
