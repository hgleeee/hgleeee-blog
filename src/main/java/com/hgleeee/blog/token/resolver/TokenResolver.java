package com.hgleeee.blog.token.resolver;

import org.springframework.security.core.Authentication;

public interface TokenResolver {

    Authentication resolve(String token);
}
