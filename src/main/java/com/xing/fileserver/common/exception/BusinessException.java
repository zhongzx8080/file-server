package com.xing.fileserver.common.exception;

public class BusinessException extends RuntimeException {
    public BusinessException() {
    }

    public BusinessException(String msg) {
        super(msg);
    }
}
