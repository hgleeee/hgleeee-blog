package com.hgleeee.blog.exception;

public class PageNotFoundException extends BlogException {

    private static final String DEFAULT_MESSAGE = "요청하신 페이지는 존재하지 않습니다.";

    public PageNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
