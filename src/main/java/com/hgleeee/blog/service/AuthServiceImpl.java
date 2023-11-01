package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.CustomUserDetails;
import com.hgleeee.blog.domain.RefreshToken;
import com.hgleeee.blog.domain.Role;
import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.dto.LoginRequestDto;
import com.hgleeee.blog.dto.SignUpRequestDto;
import com.hgleeee.blog.dto.TokenRequestDto;
import com.hgleeee.blog.dto.TokenResponseDto;
import com.hgleeee.blog.exception.DuplicateEmailException;
import com.hgleeee.blog.exception.InvalidTokenException;
import com.hgleeee.blog.exception.RefreshTokenNotFoundException;
import com.hgleeee.blog.repository.RefreshTokenRepository;
import com.hgleeee.blog.repository.UserRepository;
import com.hgleeee.blog.token.CustomAuthenticationToken;
import com.hgleeee.blog.token.TokenProvider;
import com.hgleeee.blog.token.resolver.TokenResolver;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final TokenResolver tokenResolver;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication unAuthenticatedToken =
                new CustomAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        Authentication authenticatedToken = authenticationManager.authenticate(unAuthenticatedToken);
        String accessToken = tokenProvider.createSimpleAccessToken(authenticatedToken);
        String refreshToken = tokenProvider.createSimpleRefreshToken(authenticatedToken);
        refreshTokenRepository.save(RefreshToken.builder()
                        .key(authenticatedToken.getName())
                        .value(refreshToken)
                        .build());

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Override
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        String requestedAccessToken = tokenRequestDto.getAccessToken();
        String requestedRefreshToken = tokenRequestDto.getRefreshToken();

        Authentication authentication = tokenResolver.resolve(requestedAccessToken);
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (!refreshToken.getValue().equals(requestedRefreshToken)) {
            throw new InvalidTokenException();
        }

        String reissuedAccessToken = createReissuedAccessToken(authentication);
        String reissuedRefreshToken = createReissuedRefreshToken(authentication);
        refreshToken.updateToken(reissuedRefreshToken);

        return TokenResponseDto.builder()
                .accessToken(reissuedAccessToken)
                .refreshToken(reissuedRefreshToken)
                .build();
    }

    private String createReissuedAccessToken(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return tokenProvider.createSimpleAccessToken(authentication);
        }
        return tokenProvider.createOAuth2AccessToken(authentication);
    }

    private String createReissuedRefreshToken(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return tokenProvider.createSimpleRefreshToken(authentication);
        }
        return tokenProvider.createOAuth2RefreshToken(authentication);
    }

}
