package com.hgleeee.blog.token.resolver;

import com.hgleeee.blog.token.CustomAuthenticationToken;
import com.hgleeee.blog.token.JwtConst;
import com.hgleeee.blog.token.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2TokenResolver implements TokenResolver {

    private final JwtProperties jwtProperties;

    @Override
    public Authentication resolve(String token) {
        Claims claims;
        try {
            claims = Jwts
                    .parserBuilder()
                    .setSigningKey(jwtProperties.getOauth2AccessTokenSecret())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtConst.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        OAuth2User principal = new DefaultOAuth2User(authorities, claims, "email");
        return new CustomAuthenticationToken(principal, token, authorities);
    }
}
