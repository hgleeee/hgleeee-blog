package com.hgleeee.blog.exception;

public class InvalidTokenException extends BlogException {

    private static final String DEFAULT_MESSAGE = "토큰이 유효하지 않습니다.";

    public InvalidTokenException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
