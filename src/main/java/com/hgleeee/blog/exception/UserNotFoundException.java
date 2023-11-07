package com.hgleeee.blog.exception;

public class UserNotFoundException extends BlogException {

    private static final String DEFAULT_MESSAGE = "해당 사용자를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
