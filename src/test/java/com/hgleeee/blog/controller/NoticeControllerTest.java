package com.hgleeee.blog.controller;

import com.hgleeee.blog.WithMockCustomUser;
import com.hgleeee.blog.domain.Comment;
import com.hgleeee.blog.domain.Post;
import com.hgleeee.blog.domain.Role;
import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.dto.request.CommentRequestDto;
import com.hgleeee.blog.repository.CommentRepository;
import com.hgleeee.blog.repository.NoticeRepository;
import com.hgleeee.blog.repository.PostRepository;
import com.hgleeee.blog.repository.UserRepository;
import com.hgleeee.blog.service.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NoticeControllerTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MockMvc mockMvc;

    @AfterEach
    void init() {
        noticeRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("알림 가져오기 성공 - 게시글에 댓글 다는 시나리오")
    @WithMockCustomUser
    void getNoticesWithSuccess_commentUnderPost() throws Exception {
        // given
        final User user1 = User.builder()
                .name("hgleeee")
                .email("test@test.com")
                .role(Role.ADMIN)
                .build();
        userRepository.save(user1);

        final User user2 = User.builder()
                .name("kim")
                .email("kim@test.com")
                .role(Role.USER)
                .build();
        userRepository.save(user2);

        final Post post = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .user(user1)
                .build();
        postRepository.save(post);

        final CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .postId(post.getId())
                .content("댓글입니다")
                .build();
        commentService.register("kim@test.com", commentRequestDto);

        // when - then
        mockMvc.perform(get("/api/notice")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].postId").value(post.getId()))
                .andExpect(jsonPath("$[0].message").value("kim님이 hgleeee님의 게시글에 댓글을 남겼습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("알림 가져오기 성공 - 자신의 게시글에 댓글을 다는 것은 알림이 오지 않아야 한다.")
    @WithMockCustomUser
    void getNoticesWithSuccess_commentUnderPost_NoNotice() throws Exception {
        // given
        final User user1 = User.builder()
                .name("hgleeee")
                .email("test@test.com")
                .role(Role.ADMIN)
                .build();
        userRepository.save(user1);

        final Post post = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .user(user1)
                .build();
        postRepository.save(post);

        final CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .postId(post.getId())
                .content("댓글입니다")
                .build();
        commentService.register("test@test.com", commentRequestDto);

        // when - then
        mockMvc.perform(get("/api/notice")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("알림 가져오기 성공 - 댓글에 답글 다는 시나리오")
    @WithMockCustomUser
    void getNoticesWithSuccess_commentUnderComment() throws Exception {
        // given
        final User user1 = User.builder()
                .name("hgleeee")
                .email("test@test.com")
                .role(Role.ADMIN)
                .build();
        userRepository.save(user1);

        final User user2 = User.builder()
                .name("kim")
                .email("kim@test.com")
                .role(Role.USER)
                .build();
        userRepository.save(user2);

        final Post post = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .user(user1)
                .build();
        postRepository.save(post);

        final Comment comment = Comment.builder()
                .user(user1)
                .level(0)
                .content("원댓글입니다")
                .post(post)
                .build();
        commentRepository.save(comment);

        final CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .postId(post.getId())
                .content("댓글입니다")
                .parentCommentId(comment.getId())
                .build();
        commentService.register("kim@test.com", commentRequestDto);

        // when - then
        mockMvc.perform(get("/api/notice")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].postId").value(post.getId()))
                .andExpect(jsonPath("$[0].message").value("kim님이 hgleeee님의 댓글에 답글을 남겼습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("알림 가져오기 성공 - 자신의 댓글에 답글을 다는 것은 알림이 오지 않아야 한다.")
    @WithMockCustomUser
    void getNoticesWithSuccess_commentUnderComment_NoNotice() throws Exception {
        // given
        final User user1 = User.builder()
                .name("hgleeee")
                .email("test@test.com")
                .role(Role.ADMIN)
                .build();
        userRepository.save(user1);

        final Post post = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .user(user1)
                .build();
        postRepository.save(post);

        final Comment comment = Comment.builder()
                .user(user1)
                .level(0)
                .content("원댓글입니다")
                .post(post)
                .build();
        commentRepository.save(comment);

        final CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .postId(post.getId())
                .content("댓글입니다")
                .parentCommentId(comment.getId())
                .build();
        commentService.register("test@test.com", commentRequestDto);

        // when - then
        mockMvc.perform(get("/api/notice")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist())
                .andDo(print());
    }
}