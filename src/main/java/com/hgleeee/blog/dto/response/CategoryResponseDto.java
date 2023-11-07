package com.hgleeee.blog.dto.response;

import com.hgleeee.blog.domain.Category;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryResponseDto {

    private String code;
    private String parentCategoryCode;
    private String name;
    private int level;

    @Builder
    public CategoryResponseDto(String code, String parentCategoryCode, String name, int level) {
        this.code = code;
        this.parentCategoryCode = parentCategoryCode;
        this.name = name;
        this.level = level;
    }

    public static CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .code(category.getCode())
                .level(category.getLevel())
                .name(category.getName())
                .parentCategoryCode((category.getParentCategory() == null) ? null : category.getParentCategory().getCode())
                .build();
    }
}
