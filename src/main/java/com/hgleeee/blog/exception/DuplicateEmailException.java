package com.hgleeee.blog.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends BlogException {

    private static final String DEFAULT_MESSAGE = "중복된 이메일입니다.";

    public DuplicateEmailException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
