package com.hgleeee.blog.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoticeResponseDto {

    private Long postId;
    private String message;
    private Boolean confirmed;

    @Builder
    public NoticeResponseDto(Long postId, String message, Boolean confirmed) {
        this.postId = postId;
        this.message = message;
        this.confirmed = confirmed;
    }
}
