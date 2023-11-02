package com.hgleeee.blog.oauth2;

import com.hgleeee.blog.domain.File;
import com.hgleeee.blog.domain.ProfileImage;
import com.hgleeee.blog.domain.Role;
import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.oauth2.converter.UserInfoConverter;
import com.hgleeee.blog.repository.FileRepository;
import com.hgleeee.blog.repository.ProfileImageRepository;
import com.hgleeee.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserInfoConverter<CustomOAuth2UserRequest, OAuth2UserInfo> converter;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final ProfileImageRepository profileImageRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        CustomOAuth2UserRequest customOAuth2UserRequest =
                new CustomOAuth2UserRequest(oAuth2UserRequest.getClientRegistration(), oAuth2User);
        OAuth2UserInfo oAuth2UserInfo = converter.convert(customOAuth2UserRequest);

        if (oAuth2UserInfo == null) {
            throw new OAuth2AuthenticationException("Userinfo로의 convert에 실패하였습니다.");
        }

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            OAuth2Error oauth2Error = new OAuth2Error("email_not_found",
                    "Google에 등록되지 않은 이메일입니다.", null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.getDescription());
        }

        if (!userRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
            registerOAuth2User(oAuth2UserRequest, oAuth2UserInfo);
        }

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(Role.USER.getName())),
                oAuth2User.getAttributes(),
                oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
    }

    private void registerOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        File file = File.builder()
                .url(String.valueOf(oAuth2UserInfo.getImageUrl()))
                .name("profile_" + oAuth2UserInfo.getEmail())
                .build();
        fileRepository.save(file);

        User user = User.builder()
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .role(Role.USER)
                .build();
        userRepository.save(user);

        ProfileImage profileImage = ProfileImage.builder()
                .file(file)
                .user(user)
                .build();
        profileImageRepository.save(profileImage);
    }


}
