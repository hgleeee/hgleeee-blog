package com.hgleeee.blog.oauth2;

import java.util.Map;

public class NaverUserInfo extends OAuth2UserInfo {

    public NaverUserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response"));
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getName() {
        return (String) getAttributes().get("name");
    }

    @Override
    public String getImageUrl() {
        return (String) getAttributes().get("profile_image");
    }

    @Override
    public String getEmail() {
        return (String) getAttributes().get("email");
    }
}
