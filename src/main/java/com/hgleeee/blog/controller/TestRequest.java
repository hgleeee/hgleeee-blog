package com.hgleeee.blog.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TestRequest {

    @Schema(description = "아이디")
    private Integer id;

    @Schema(description = "이름")
    private String name;

    @Builder
    public TestRequest(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
