package com.simin.approval.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {}
    public TokenExpiredException(String msg) {
        super(msg);
    }
    public TokenExpiredException(String msg, Throwable e) {
        super(msg);
    }
}
