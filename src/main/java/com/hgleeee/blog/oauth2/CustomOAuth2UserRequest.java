package com.hgleeee.blog.oauth2;

import lombok.Getter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomOAuth2UserRequest {

    private final ClientRegistration clientRegistration;
    private final OAuth2User oAuth2User;

    public CustomOAuth2UserRequest(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        this.clientRegistration = clientRegistration;
        this.oAuth2User = oAuth2User;
    }
}
