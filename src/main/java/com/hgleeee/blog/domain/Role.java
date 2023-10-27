package com.hgleeee.blog.domain;

public enum Role {

    ADMIN("admin"),
    USER("user");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
