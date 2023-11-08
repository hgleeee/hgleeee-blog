package com.hgleeee.blog.exception;

public class LocalFileException extends BlogException {

    private static final String DEFAULT_MESSAGE = "파일 저장에 실패했습니다";

    public LocalFileException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
