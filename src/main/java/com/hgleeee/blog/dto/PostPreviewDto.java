package com.hgleeee.blog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostPreviewDto {

    private String title;
    private String authorName;
    private LocalDateTime createdAt;
    private int viewCount;

    @Builder
    public PostPreviewDto(String title, String authorName, LocalDateTime createdAt, int viewCount) {
        this.title = title;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }
}
