package com.hgleeee.blog.token.resolver;

import com.hgleeee.blog.domain.CustomUserDetails;
import com.hgleeee.blog.token.CustomAuthenticationToken;
import com.hgleeee.blog.token.JwtConst;
import com.hgleeee.blog.token.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SimpleTokenResolver implements TokenResolver {

    private final JwtProperties jwtProperties;

    @Override
    public Authentication resolve(String token) {
        Claims claims;
        try {
            claims = Jwts
                    .parserBuilder()
                    .setSigningKey(jwtProperties.getSimpleAccessTokenSecret())
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

        UserDetails principal = new CustomUserDetails(String.valueOf(claims.get("email")), "", authorities);
        return new CustomAuthenticationToken(principal, token, authorities);
    }

}
