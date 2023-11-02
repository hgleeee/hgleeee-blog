package com.hgleeee.blog.exception;

public class RefreshTokenNotFoundException extends BlogException {

    private static final String DEFAULT_MESSAGE = "Refresh Token이 존재하지 않습니다. 로그인이 필요합니다.";

    public RefreshTokenNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
