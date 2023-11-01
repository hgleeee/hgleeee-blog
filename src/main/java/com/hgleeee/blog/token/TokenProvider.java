package com.hgleeee.blog.token;

import com.hgleeee.blog.util.EmailExtractor;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private final Key simpleAccessTokenKey;
    private final Key simpleRefreshTokenKey;
    private final Key oAuth2AccessTokenKey;
    private final Key oAuth2RefreshTokenKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    @Autowired
    public TokenProvider(JwtProperties jwtProperties) {
        this.accessTokenExpirationTime = jwtProperties.getAccessTokenExpirationTime();
        this.refreshTokenExpirationTime = jwtProperties.getRefreshTokenExpirationTime();
        this.simpleAccessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSimpleAccessTokenSecret()));
        this.simpleRefreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSimpleRefreshTokenSecret()));
        this.oAuth2AccessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getOauth2AccessTokenSecret()));
        this.oAuth2RefreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getOauth2RefreshTokenSecret()));
    }

    public String createSimpleAccessToken(Authentication authentication) {
        return createToken(authentication, accessTokenExpirationTime, simpleAccessTokenKey);
    }

    public String createSimpleRefreshToken(Authentication authentication) {
        return createToken(authentication, refreshTokenExpirationTime, simpleRefreshTokenKey);
    }

    public String createOAuth2AccessToken(Authentication authentication) {
        return createToken(authentication, accessTokenExpirationTime, oAuth2AccessTokenKey);
    }

    public String createOAuth2RefreshToken(Authentication authentication) {
        return createToken(authentication, refreshTokenExpirationTime, oAuth2RefreshTokenKey);
    }

    private String createToken(Authentication authentication,
                               long tokenExpirationTime, Key key) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put(JwtConst.AUTHORITIES_KEY,
                authentication.getAuthorities()
                        .stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(",")));
        claims.put("email", EmailExtractor.extract(authentication));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key)
                .setExpiration(createExpireDate(tokenExpirationTime))
                .compact();
    }

    private Date createExpireDate(long tokenExpirationTime) {
        Date now = new Date();
        return new Date(now.getTime() + tokenExpirationTime);
    }

    private boolean isValid(String token, Key key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("JWT 형식이 잘못되었습니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰 잘못됨");
        }
        return false;
    }

}
