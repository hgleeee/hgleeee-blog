package com.hgleeee.blog.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/abcd")
    public Authentication test(Authentication authentication) {
        return authentication;
    }
}
