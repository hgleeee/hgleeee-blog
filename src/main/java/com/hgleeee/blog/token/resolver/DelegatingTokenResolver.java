package com.hgleeee.blog.token.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import java.util.List;

@RequiredArgsConstructor
public class DelegatingTokenResolver implements TokenResolver {

    private final List<TokenResolver> tokenResolvers;

    @Override
    public Authentication resolve(String token) {
        for (TokenResolver tokenResolver : tokenResolvers) {
            Authentication authentication = tokenResolver.resolve(token);
            if (authentication != null) {
                return authentication;
            }
        }
        return null;
    }
}
