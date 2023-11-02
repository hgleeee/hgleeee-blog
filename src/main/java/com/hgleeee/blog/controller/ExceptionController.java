package com.hgleeee.blog.controller;

import com.hgleeee.blog.dto.ErrorResponseDto;
import com.hgleeee.blog.exception.BlogException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ErrorResponseDto> blogException(BlogException e) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(String.valueOf(e.getStatusCode()))
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getStatusCode())
                .body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> bindException(MethodArgumentNotValidException e) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(String.valueOf(e.getStatusCode().value()))
                .message(e.getMessage())
                .build();
        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponseDto.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(e.getStatusCode().value())
                .body(errorResponseDto);
    }
}
