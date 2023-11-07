package com.hgleeee.blog.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentUpdateRequestDto {

    @NotNull(message = "댓글 id는 필수입니다.")
    private Long commentId;

    @NotEmpty(message = "content 값은 필수입니다.")
    private String content;

    @Builder
    public CommentUpdateRequestDto(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
