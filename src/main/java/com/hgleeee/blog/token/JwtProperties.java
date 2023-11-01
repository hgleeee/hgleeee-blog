package com.hgleeee.blog.token;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {

    @Value("${jwt.simple-access-token-secret}")
    private String simpleAccessTokenSecret;

    @Value("${jwt.simple-refresh-token-secret}")
    private String simpleRefreshTokenSecret;

    @Value("${jwt.oauth2-access-token-secret}")
    private String oauth2AccessTokenSecret;

    @Value("${jwt.oauth2-refresh-token-secret}")
    private String oauth2RefreshTokenSecret;

    @Value("${jwt.access-token-expiration-time}")
    private long accessTokenExpirationTime;

    @Value("${jwt.refresh-token-expiration-time}")
    private long refreshTokenExpirationTime;

}
