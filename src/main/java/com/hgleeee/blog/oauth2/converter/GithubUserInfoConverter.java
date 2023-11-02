package com.hgleeee.blog.oauth2.converter;

import com.hgleeee.blog.oauth2.CustomOAuth2UserRequest;
import com.hgleeee.blog.oauth2.GithubUserInfo;
import com.hgleeee.blog.oauth2.OAuth2UserInfo;

public class GithubUserInfoConverter implements UserInfoConverter<CustomOAuth2UserRequest, OAuth2UserInfo> {

    @Override
    public OAuth2UserInfo convert(CustomOAuth2UserRequest customOAuth2UserRequest) {
        if (customOAuth2UserRequest.getClientRegistration().getRegistrationId().equals("github")) {
            return new GithubUserInfo(customOAuth2UserRequest.getOAuth2User().getAttributes());
        }
        return null;
    }

}
