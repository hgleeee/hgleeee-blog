package com.hgleeee.blog.dto.request;

import com.hgleeee.blog.domain.Comment;
import com.hgleeee.blog.domain.Post;
import com.hgleeee.blog.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDto {

    private Long postId;
    private Long parentCommentId;
    private String content;

    @Builder
    public CommentRequestDto(Long postId, Long parentCommentId, String content) {
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
    }
    public Comment toEntity(User user, Post post, Comment parentComment) {
        return Comment.builder()
                .user(user)
                .post(post)
                .parentComment(parentComment)
                .content(content)
                .build();
    }
}
