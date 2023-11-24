package com.hgleeee.blog.dto.response;

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

    @Builder
    public PostResponseDto(Long id, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
