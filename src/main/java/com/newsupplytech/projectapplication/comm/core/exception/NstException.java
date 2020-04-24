package com.newsupplytech.projectapplication.comm.core.exception;

public class NstException extends RuntimeException {
    public NstException() {
    }

    public NstException(String message) {
        super(message);
    }

    public NstException(String message, Throwable cause) {
        super(message, cause);
    }

    public NstException(Throwable cause) {
        super(cause);
    }

    public NstException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
