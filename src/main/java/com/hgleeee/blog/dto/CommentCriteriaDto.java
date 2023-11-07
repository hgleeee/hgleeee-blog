package com.hgleeee.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentCriteriaDto {

    private Long postId;
    private int pageNo = 1;
    private int pageSize = 10;

    public CommentCriteriaDto(Long postId, int pageNo, int pageSize) {
        this.postId = postId;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
