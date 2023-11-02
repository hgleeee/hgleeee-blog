package com.hgleeee.blog.token.resolver;

import com.hgleeee.blog.token.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "tokenResolver")
public class DelegatingTokenResolver implements TokenResolver {

    private final JwtProperties jwtProperties;
    private final List<TokenResolver> tokenResolvers;

    @Autowired
    public DelegatingTokenResolver(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.tokenResolvers = List.of(new SimpleTokenResolver(jwtProperties),
                new OAuth2TokenResolver(jwtProperties));
    }

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
