package com.hgleeee.blog.exception;

public class NoAuthorityException extends BlogException {

    private static final String DEFAULT_MESSAGE = "권한이 없습니다";

    public NoAuthorityException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
