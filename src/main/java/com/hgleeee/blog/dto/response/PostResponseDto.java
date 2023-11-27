package com.hgleeee.blog.dto.response;

import com.hgleeee.blog.domain.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int viewCount;

    @Builder
    public PostResponseDto(Long id, String title, String content, LocalDateTime createdAt, int viewCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }

    public static PostResponseDto createPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getViewCount())
                .build();
    }
}
