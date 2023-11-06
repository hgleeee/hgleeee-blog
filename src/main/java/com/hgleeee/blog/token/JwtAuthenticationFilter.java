package com.hgleeee.blog.token;

import com.hgleeee.blog.token.resolver.TokenResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenResolver tokenResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = resolveToken(request);
        log.debug("jwt = {}", jwt);
        if (StringUtils.hasText(jwt)) {
            Authentication authentication = tokenResolver.resolve(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtConst.HEADER_STRING);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConst.TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }


}
