package com.hgleeee.blog.exception;

public class CategoryNotFoundException extends BlogException {

    private static final String DEFAULT_MESSAGE = "존재하지 않는 카테고리에 접근 시도";

    public CategoryNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
