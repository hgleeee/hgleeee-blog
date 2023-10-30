package com.hgleeee.blog.token;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {

    public static final String AUTHORITIES_KEY = "auth";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

}
