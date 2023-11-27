package com.hgleeee.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SearchCriteriaDto {

    @Schema(description = "검색 키워드 (제목 한정)", nullable = true)
    private String category;

    @Schema(description = "한 번에 불러올 글 개수", defaultValue = "10", nullable = true)
    private long pageSize = 10;

    @Schema(description = "페이지 no", defaultValue = "1", nullable = true)
    private long pageNo = 1;

    public SearchCriteriaDto(String category, long pageSize, long pageNo) {
        this.category = category;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }
}
