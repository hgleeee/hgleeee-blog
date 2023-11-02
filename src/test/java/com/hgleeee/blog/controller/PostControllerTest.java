package com.hgleeee.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgleeee.blog.WithMockCustomUser;
import com.hgleeee.blog.domain.Category;
import com.hgleeee.blog.domain.Post;
import com.hgleeee.blog.dto.PostUpdateRequestDto;
import com.hgleeee.blog.exception.PostNotFoundException;
import com.hgleeee.blog.repository.CategoryRepository;
import com.hgleeee.blog.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostRepository postRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void init() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("postId를 가지고 글 가져오기 성공")
    void getPostWithSuccess() throws Exception {
        // given
        String givenTitle = "안녕하세요";
        String givenContent = "반갑습니다";

        Post post = Post.builder()
                .title(givenTitle)
                .content(givenContent)
                .build();
        Long id = postRepository.save(post).getId();

        // when - then
        mockMvc.perform(get("/api/post/{id}", id)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(givenTitle))
                .andExpect(jsonPath("$.content").value(givenContent))
                .andDo(print());
    }

    @Test
    @DisplayName("postId를 가지고 글 가져오기 실패 - 잘못된 postId")
    void getPostWithFailure() throws Exception {
        // given
        String givenTitle = "안녕하세요";
        String givenContent = "반갑습니다";

        Post post = Post.builder()
                .title(givenTitle)
                .content(givenContent)
                .build();
        Long id = postRepository.save(post).getId();

        // when - then
        mockMvc.perform(get("/api/post/{id}", id + 1)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("글 등록 실패 - 권한 x")
    void registerWithFailureNoAuthority() throws Exception {
        mockMvc.perform(post("/api/post/register")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("글 등록 성공")
    @WithMockCustomUser
    void registerWithSuccess() throws Exception {
        mockMvc.perform(post("/api/post/register")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 실패 - 권한 x")
    void updateWithFailureNoAuthority() throws Exception {
        mockMvc.perform(patch("/api/post/update")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 실패 - id 조건 만족 x")
    @WithMockCustomUser
    void updateWithFailure_NotValidId() throws Exception {
        PostUpdateRequestDto postUpdateRequestDto = PostUpdateRequestDto.builder()
                .postId(null)
                .title("안녕하세요")
                .content("반갑습니다")
                .categoryCode("100")
                .build();

        String json = objectMapper.writeValueAsString(postUpdateRequestDto);
        // when - then
        mockMvc.perform(patch("/api/post/update")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.validation.postId").value("글 id는 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 실패 - 제목 조건 만족 x")
    @WithMockCustomUser
    void updateWithFailure_NotValidTitle() throws Exception {
        PostUpdateRequestDto postUpdateRequestDto = PostUpdateRequestDto.builder()
                .postId(1L)
                .title(null)
                .content("반갑습니다")
                .categoryCode("100")
                .build();

        String json = objectMapper.writeValueAsString(postUpdateRequestDto);
        // when - then
        mockMvc.perform(patch("/api/post/update")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.validation.title").value("title 값은 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 실패 - 내용 조건 만족 x")
    @WithMockCustomUser
    void updateWithFailure_NotValidContent() throws Exception {
        PostUpdateRequestDto postUpdateRequestDto = PostUpdateRequestDto.builder()
                .postId(1L)
                .title("안녕하세요")
                .content(null)
                .categoryCode("100")
                .build();

        String json = objectMapper.writeValueAsString(postUpdateRequestDto);
        // when - then
        mockMvc.perform(patch("/api/post/update")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.validation.content").value("content 값은 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 실패 - 카테고리 코드 조건 만족 x")
    @WithMockCustomUser
    void updateWithFailure_NotValidCategoryCode() throws Exception {
        PostUpdateRequestDto postUpdateRequestDto = PostUpdateRequestDto.builder()
                .postId(1L)
                .title("안녕하세요")
                .content("반갑습니다")
                .categoryCode(null)
                .build();

        String json = objectMapper.writeValueAsString(postUpdateRequestDto);
        // when - then
        mockMvc.perform(patch("/api/post/update")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.validation.categoryCode").value("카테고리를 선택해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 성공")
    @WithMockCustomUser
    void updateWithSuccess() throws Exception {
        // given
        Post savedPost = postRepository.save(Post.builder()
                        .title("")
                        .content("")
                        .build());
        categoryRepository.save(Category.builder()
                .code("100")
                .level(1)
                .name("Spring-boot")
                .build());

        System.out.println(savedPost.getId());

        PostUpdateRequestDto postUpdateRequestDto = PostUpdateRequestDto.builder()
                .postId(savedPost.getId())
                .title("안녕하세요")
                .content("반갑습니다")
                .categoryCode("100")
                .build();

        String json = objectMapper.writeValueAsString(postUpdateRequestDto);

        // when - then
        mockMvc.perform(patch("/api/post/update")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        Post post = postRepository.findById(savedPost.getId())
                .orElseThrow(PostNotFoundException::new);
        Assertions.assertEquals("안녕하세요", post.getTitle());
        Assertions.assertEquals("반갑습니다", post.getContent());
    }

}