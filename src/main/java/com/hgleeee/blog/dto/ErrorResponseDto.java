package com.hgleeee.blog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorResponseDto {

    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    @Builder
    public ErrorResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
