package com.hgleeee.blog.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostUpdateRequestDto {

    @NotNull(message = "글 id는 필수입니다.")
    private Long postId;

    @NotEmpty(message = "title 값은 필수입니다.")
    private String title;

    @NotEmpty(message = "content 값은 필수입니다.")
    private String content;

    @NotEmpty(message = "카테고리를 선택해주세요.")
    private String categoryCode;

    @Builder
    public PostUpdateRequestDto(Long postId, String title, String content, String categoryCode) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.categoryCode = categoryCode;
    }
}
