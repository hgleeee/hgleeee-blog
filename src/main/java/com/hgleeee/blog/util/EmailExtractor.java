package com.hgleeee.blog.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class EmailExtractor {

    public static final String extract(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            return ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
        }
        return authentication.getName();
    }
}
