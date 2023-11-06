package com.hgleeee.blog.exception;

public class PostNotFoundException extends BlogException {

    private static final String DEFAULT_MESSAGE = "해당 글을 찾을 수 없습니다";

    public PostNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
