package com.hgleeee.blog.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgleeee.blog.domain.RefreshToken;
import com.hgleeee.blog.dto.TokenResponseDto;
import com.hgleeee.blog.repository.RefreshTokenRepository;
import com.hgleeee.blog.token.TokenProvider;
import com.hgleeee.blog.util.EmailExtractor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if (logger.isDebugEnabled()) {
            logger.debug(authentication.getPrincipal());
        }

        String accessToken = tokenProvider.createOAuth2AccessToken(authentication);
        String refreshToken = tokenProvider.createOAuth2RefreshToken(authentication);
        TokenResponseDto tokenResponse = TokenResponseDto.builder()
                .accessToken(accessToken).refreshToken(refreshToken).build();
        refreshTokenRepository.save(RefreshToken.builder()
                .key(EmailExtractor.extract(authentication))
                .value(refreshToken)
                .build());
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
    }
}
