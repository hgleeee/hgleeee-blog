package com.hgleeee.blog.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PostsPreviewDto {

    private List<PostPreviewDto> posts;
    private long totalPostCount;

    @Builder
    public PostsPreviewDto(List<PostPreviewDto> posts, long totalPostCount) {
        this.posts = posts;
        this.totalPostCount = totalPostCount;
    }

    @Data
    @NoArgsConstructor
    public static class PostPreviewDto {
        private Long id;
        private String categoryName;
        private String title;
        private String authorName;
        private LocalDateTime createdAt;
        private int viewCount;

        @Builder
        public PostPreviewDto(Long id, String categoryName, String title,
                              String authorName, LocalDateTime createdAt, int viewCount) {
            this.id = id;
            this.categoryName = categoryName;
            this.title = title;
            this.authorName = authorName;
            this.createdAt = createdAt;
            this.viewCount = viewCount;
        }
    }
}
