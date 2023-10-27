package com.hgleeee.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "TestController")
public class TestController {

    @Operation(summary = "테스트 요청", description = "테스트 요청을 보냅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public TestResponse test(@RequestBody TestRequest testRequest) {
        return TestResponse.builder()
                .id(testRequest.getId())
                .name(testRequest.getName())
                .build();
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
