package com.hgleeee.blog.controller;

import com.hgleeee.blog.dto.LoginRequestDto;
import com.hgleeee.blog.dto.SignUpRequestDto;
import com.hgleeee.blog.dto.TokenResponseDto;
import com.hgleeee.blog.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        Long id = authService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

}
