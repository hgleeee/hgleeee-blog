package com.hgleeee.blog.oauth2.converter;

import com.hgleeee.blog.oauth2.CustomOAuth2UserRequest;
import com.hgleeee.blog.oauth2.OAuth2UserInfo;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class DelegatingUserInfoConverter implements UserInfoConverter<CustomOAuth2UserRequest, OAuth2UserInfo> {

    private final List<UserInfoConverter<CustomOAuth2UserRequest, OAuth2UserInfo>> converters;

    public DelegatingUserInfoConverter() {
        List<UserInfoConverter<CustomOAuth2UserRequest, OAuth2UserInfo>> providerUserConverters = Arrays.asList(
                new GoogleUserInfoConverter(),
                new NaverUserInfoConverter(),
                new GithubUserInfoConverter()
        );

        this.converters = Collections.unmodifiableList(new LinkedList<>(providerUserConverters));
    }

    @Override
    public OAuth2UserInfo convert(CustomOAuth2UserRequest customOAuth2UserRequest) {
        for (UserInfoConverter<CustomOAuth2UserRequest, OAuth2UserInfo> converter : converters) {
            OAuth2UserInfo oAuth2UserInfo = converter.convert(customOAuth2UserRequest);
            if (oAuth2UserInfo != null) {
                return oAuth2UserInfo;
            }
        }
        return null;
    }

}
