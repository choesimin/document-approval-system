package com.simin.approval.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException() {}
    public NoDataFoundException(String msg) {
        super(msg);
    }
    public NoDataFoundException(String msg, Throwable e) {
        super(msg);
    }
}
