package com.simin.approval.exception;

public class UnsuitableDataException extends RuntimeException {
    public UnsuitableDataException() {}
    public UnsuitableDataException(String msg) {
        super(msg);
    }
    public UnsuitableDataException(String msg, Throwable e) {
        super(msg);
    }
}
