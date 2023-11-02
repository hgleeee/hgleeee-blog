package com.hgleeee.blog.exception;

import org.springframework.http.HttpStatus;

public class EmailPasswordInvalidException extends BlogException {

    private static final String DEFAULT_MESSAGE = "ID 혹은 Password를 확인해주세요.";

    public EmailPasswordInvalidException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
