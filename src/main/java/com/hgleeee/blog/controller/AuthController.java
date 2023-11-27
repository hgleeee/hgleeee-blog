package com.hgleeee.blog.controller;

import com.hgleeee.blog.dto.request.LoginRequestDto;
import com.hgleeee.blog.dto.request.SignUpRequestDto;
import com.hgleeee.blog.dto.request.TokenRequestDto;
import com.hgleeee.blog.dto.response.TokenResponseDto;
import com.hgleeee.blog.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        TokenResponseDto tokenResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        Long id = authService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        TokenResponseDto tokenResponseDto = authService.reissue(tokenRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
    }

}
