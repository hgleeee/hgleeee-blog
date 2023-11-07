package com.hgleeee.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgleeee.blog.WithMockCustomUser;
import com.hgleeee.blog.domain.*;
import com.hgleeee.blog.dto.request.CommentRequestDto;
import com.hgleeee.blog.dto.request.CommentUpdateRequestDto;
import com.hgleeee.blog.exception.CommentNotFoundException;
import com.hgleeee.blog.repository.CategoryRepository;
import com.hgleeee.blog.repository.CommentRepository;
import com.hgleeee.blog.repository.PostRepository;
import com.hgleeee.blog.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostRepository postRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;

    Category category;
    Post post;
    User user;

    @BeforeEach
    void init() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        category = Category.builder()
                .code("100")
                .level(1)
                .order(0)
                .name("spring")
                .build();
        category = categoryRepository.save(category);

        user = User.builder()
                .name("lee")
                .email("test@test.com")
                .role(Role.USER)
                .build();
        user = userRepository.save(user);

        post = Post.builder()
                .category(category)
                .title("제목입니다")
                .content("내용입니다")
                .user(user)
                .build();
        post = postRepository.save(post);
    }

    @AfterEach
    void after() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("조건에 맞는 댓글 불러오기 성공")
    void getComments() throws Exception {
        // given
        List<Comment> comments = commentRepository.saveAll(IntStream.range(0, 10)
                .mapToObj(i -> Comment.builder()
                        .user(user)
                        .content("댓글입니다 " + i)
                        .post(post)
                        .build())
                .collect(Collectors.toList()));
        Collections.sort(comments);

        commentRepository.saveAll(IntStream.range(10, 13)
                .mapToObj(i -> Comment.builder()
                        .user(user)
                        .content("댓글입니다 " + i)
                        .post(post)
                        .level(1)
                        .parentComment(comments.get(1))
                        .build())
                .collect(Collectors.toList()));

        // when - then
        mockMvc.perform(get("/api/comment/comments")
                        .param("postId", String.valueOf(post.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 등록 실패 - 권한 x")
    void registerWithFailure_NoAuthority() throws Exception {
        // given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .postId(post.getId())
                .content("댓글입니다")
                .build();
        String json = objectMapper.writeValueAsString(commentRequestDto);

        // when-then
        mockMvc.perform(post("/api/comment/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 등록 성공")
    void registerWithSuccess() throws Exception {
        // given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .postId(post.getId())
                .content("댓글입니다")
                .build();
        String json = objectMapper.writeValueAsString(commentRequestDto);

        // when-then
        mockMvc.perform(post("/api/comment/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(print());

        Assertions.assertEquals(1, commentRepository.count());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("대댓글 등록 성공")
    void registerChildCommentWithSuccess() throws Exception {
        // given
        Comment parentComment = commentRepository.save(Comment.builder()
                .content("부모댓글입니다")
                .level(0)
                .user(user)
                .post(post)
                .build());
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .postId(post.getId())
                .parentCommentId(parentComment.getId())
                .content("대댓글입니다")
                .build();
        String json = objectMapper.writeValueAsString(commentRequestDto);

        // when-then
        mockMvc.perform(post("/api/comment/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andDo(print());

        Assertions.assertEquals(2, commentRepository.count());
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            if (comment.getParentComment() != null) {
                Assertions.assertEquals("대댓글입니다", comment.getContent());
                Assertions.assertEquals(1, comment.getLevel());
            }
        }
    }

    @Test
    @DisplayName("댓글 수정 실패 - 권한 x")
    void updateWithFailure_NoAuthority() throws Exception {
        // given
        Comment comment = commentRepository.save(Comment.builder()
                .content("원댓글입니다")
                .level(0)
                .user(user)
                .post(post)
                .build());

        CommentUpdateRequestDto commentUpdateRequestDto = CommentUpdateRequestDto.builder()
                .commentId(comment.getId())
                .content("바뀐댓글입니다")
                .build();
        String json = objectMapper.writeValueAsString(commentUpdateRequestDto);

        // when-then
        mockMvc.perform(patch("/api/comment/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 수정 성공")
    void updateWithSuccess() throws Exception {
        // given
        Comment comment = commentRepository.save(Comment.builder()
                .content("원댓글입니다")
                .level(0)
                .user(user)
                .post(post)
                .build());

        CommentUpdateRequestDto commentUpdateRequestDto = CommentUpdateRequestDto.builder()
                .commentId(comment.getId())
                .content("바뀐댓글입니다")
                .build();
        String json = objectMapper.writeValueAsString(commentUpdateRequestDto);

        // when-then
        mockMvc.perform(patch("/api/comment/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        Comment savedComment = commentRepository.findById(comment.getId())
                .orElseThrow(CommentNotFoundException::new);
        Assertions.assertEquals("바뀐댓글입니다", savedComment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 권한 x")
    void deleteWithFailure_NoAuthority() throws Exception {
        // given
        Comment comment = commentRepository.save(Comment.builder()
                .content("원댓글입니다")
                .level(0)
                .user(user)
                .post(post)
                .build());

        // when-then
        mockMvc.perform(delete("/api/comment/delete")
                        .param("commentId", String.valueOf(comment.getId())))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 삭제 성공")
    void deleteWithSuccess() throws Exception {
        // given
        Comment comment = commentRepository.save(Comment.builder()
                .content("원댓글입니다")
                .level(0)
                .user(user)
                .post(post)
                .build());

        // when-then
        mockMvc.perform(delete("/api/comment/delete?commentId=" + comment.getId()))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(1, commentRepository.count());
        Assertions.assertNotNull(commentRepository.findById(comment.getId()).get().getDeletedAt());
    }
}