package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.LoginRequestDto;
import com.hgleeee.blog.dto.SignUpRequestDto;
import com.hgleeee.blog.dto.TokenResponseDto;

public interface AuthService {

    TokenResponseDto login(LoginRequestDto loginRequestDto);
    Long signUp(SignUpRequestDto signUpRequestDto);
}
