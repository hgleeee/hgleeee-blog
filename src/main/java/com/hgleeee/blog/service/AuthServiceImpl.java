package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.dto.LoginRequestDto;
import com.hgleeee.blog.dto.SignUpRequestDto;
import com.hgleeee.blog.dto.TokenResponseDto;
import com.hgleeee.blog.exception.DuplicateEmailException;
import com.hgleeee.blog.repository.UserRepository;
import com.hgleeee.blog.token.CustomAuthenticationToken;
import com.hgleeee.blog.token.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication unAuthenticatedToken =
                new CustomAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        Authentication authenticatedToken = authenticationManager.authenticate(unAuthenticatedToken);
        String accessToken = tokenProvider.createToken(authenticatedToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public Long signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new DuplicateEmailException();
        }

        User user = User.builder()
                .name(signUpRequestDto.getName())
                .email(signUpRequestDto.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequestDto.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
}
