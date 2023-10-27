package com.hgleeee.blog.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestResponse {

    private Integer id;
    private String name;

    @Builder
    public TestResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
