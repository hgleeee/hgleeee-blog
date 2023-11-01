package com.hgleeee.blog.oauth2;

import java.util.Map;

public class GithubUserInfo extends OAuth2UserInfo {

    public GithubUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(getAttributes().get("id"));
    }

    @Override
    public String getName() {
        return (String) getAttributes().get("login");
    }

    @Override
    public String getEmail() {
        return (String) getAttributes().get("login") + "@github.com";
    }

    @Override
    public String getImageUrl() {
        return (String) getAttributes().get("avatar_url");
    }
}
