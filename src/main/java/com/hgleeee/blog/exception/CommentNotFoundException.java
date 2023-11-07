package com.hgleeee.blog.exception;

public class CommentNotFoundException extends BlogException {

    private static final String DEFAULT_MESSAGE = "해당 댓글을 찾을 수 없습니다.";

    public CommentNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
