package com.hgleeee.blog.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CommentsResponseDto {

    private List<CommentResponseDto> comments;
    private long totalCommentCount;

    @Builder
    public CommentsResponseDto(List<CommentResponseDto> comments, long totalCommentCount) {
        this.comments = comments;
        this.totalCommentCount = totalCommentCount;
    }
}
