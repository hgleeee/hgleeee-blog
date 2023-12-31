package com.hgleeee.blog.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String authorName;
    private int level;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @Builder
    public CommentResponseDto(Long id, String authorName, int level, String content,
                              LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.authorName = authorName;
        this.level = level;
        this.content = content;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }
}
