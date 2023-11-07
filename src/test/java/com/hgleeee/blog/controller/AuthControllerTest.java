package com.hgleeee.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgleeee.blog.domain.Role;
import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.dto.LoginRequestDto;
import com.hgleeee.blog.dto.SignUpRequestDto;
import com.hgleeee.blog.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }

    @AfterEach
    void after() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void loginWithSuccess() throws Exception {
        // given
        User user = User.builder()
                .email("test@test.com")
                .name("lee")
                .password(bCryptPasswordEncoder.encode("abcd"))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("test@test.com")
                .password("abcd")
                .build();

        // when-then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void loginWithWrongEmailFailure() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("test@test.com")
                .password("abcd")
                .build();

        // when-then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("ID 혹은 Password를 확인해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 패스워드 틀림")
    void loginWithWrongPasswordFailure() throws Exception {
        // given
        User user = User.builder()
                .email("test@test.com")
                .name("lee")
                .password(bCryptPasswordEncoder.encode("abcd"))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("test@test.com")
                .password("abcde")
                .build();

        // when-then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("ID 혹은 Password를 확인해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 성공")
    void signUpWithSuccess() throws Exception {
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("lee")
                .email("test@test.com")
                .password("abcdef123")
                .build();

        String json = objectMapper.writeValueAsString(signUpRequestDto);

        // when-then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 실패 - 이메일 중복")
    void signUpWithEmailDuplicateFailure() throws Exception {
        // given
        User user = User.builder()
                .email("test@test.com")
                .name("lee")
                .password("abcd")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email("test@test.com")
                .name("choi")
                .password("abcd")
                .build();

        // when-then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("중복된 이메일입니다."))
                .andDo(print());
    }
}