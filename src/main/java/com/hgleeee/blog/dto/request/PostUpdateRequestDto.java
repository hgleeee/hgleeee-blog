package com.hgleeee.blog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostUpdateRequestDto {

    @Schema(description = "글 고유 id", example = "1")
    @NotNull(message = "글 id는 필수입니다.")
    private Long postId;

    @Schema(description = "글 제목", example = "여기는 글 제목 자리")
    @NotEmpty(message = "title 값은 필수입니다.")
    private String title;

    @Schema(description = "글 내용", example = "여기는 글 내용 자리")
    @NotEmpty(message = "content 값은 필수입니다.")
    private String content;

    @Schema(description = "카테고리 code", example = "100")
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
