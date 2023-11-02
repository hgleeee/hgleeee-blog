package com.hgleeee.blog.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

// TODO: 코드 리팩토링 필요, 확장에 있어서 유연하지 못함
public class EmailExtractor {

    public static final String extract(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            return authentication.getName();
        }
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        if (principal.getAttributes().get("response") != null) {
            return (String) ((Map<String, Object>) principal.getAttributes().get("response")).get("email");
        }

        if (principal.getAttributes().get("email") != null) {
            return (String) principal.getAttributes().get("email");
        }

        return (String) principal.getAttributes().get("login") + "@github.com";
    }
}
