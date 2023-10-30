package com.hgleeee.blog.domain;

public enum Role {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String name;

    public String getName() {
        return name;
    }

    Role(String name) {
        this.name = name;
    }
}
