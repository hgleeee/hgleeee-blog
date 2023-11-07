package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.request.LoginRequestDto;
import com.hgleeee.blog.dto.request.SignUpRequestDto;
import com.hgleeee.blog.dto.request.TokenRequestDto;
import com.hgleeee.blog.dto.response.TokenResponseDto;

public interface AuthService {

    TokenResponseDto login(LoginRequestDto loginRequestDto);
    Long signUp(SignUpRequestDto signUpRequestDto);

    TokenResponseDto reissue(TokenRequestDto tokenRequestDto);
}
