package com.hgleeee.blog.token;

import com.hgleeee.blog.domain.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private final Key accessTokenKey;
    private final Key refreshTokenKey;
    private final int accessTokenExpirationTime;
    private final int refreshTokenExpirationTime;

    @Autowired
    public TokenProvider(@Value("${jwt.access-token-secret}") String accessTokenSecret,
                         @Value("${jwt.refresh-token-secret}") String refreshTokenSecret,
                         @Value("${jwt.access-token-expiration-time}") int accessTokenExpirationTime,
                         @Value("${jwt.refresh-token-expiration-time}") int refreshTokenExpirationTime) {
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret));
        this.refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecret));
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, accessTokenExpirationTime, accessTokenKey);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, refreshTokenExpirationTime, refreshTokenKey);
    }

    private String createToken(Authentication authentication, long tokenExpirationTime, Key key) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put(JwtProperties.AUTHORITIES_KEY,
                authentication.getAuthorities()
                        .stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(",")));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, key)
                .setExpiration(createExpireDate(tokenExpirationTime))
                .compact();
    }

    private Date createExpireDate(long tokenExpirationTime) {
        Date now = new Date();
        return new Date(now.getTime() + tokenExpirationTime);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(accessTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtProperties.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new CustomUserDetails(claims.getSubject(), "", authorities);
        return new CustomAuthenticationToken(principal, token, authorities);
    }

    public boolean isAccessTokenValid(String token) {
        return isValid(token, accessTokenKey);
    }

    public boolean isRefreshTokenValid(String token) {
        return isValid(token, refreshTokenKey);
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
