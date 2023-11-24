package com.hgleeee.blog.dto.response;

import com.hgleeee.blog.domain.Category;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CategoryResponseDto {

    private String code;
    private String name;
    private int level;
    private int order;
    private List<SubCategoryResponseDto> subCategories;

    @Builder
    public CategoryResponseDto(String code, String name, int level, int order,
                               List<SubCategoryResponseDto> subCategories) {
        this.code = code;
        this.name = name;
        this.level = level;
        this.order = order;
        this.subCategories = subCategories;
    }

    public static CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .code(category.getCode())
                .level(category.getLevel())
                .order(category.getOrder())
                .name(category.getName())
                .subCategories(category.getCategories()
                        .stream()
                        .map(s -> SubCategoryResponseDto.builder()
                                .code(s.getCode())
                                .name(s.getName())
                                .level(s.getLevel())
                                .order(s.getOrder())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Data
    @NoArgsConstructor
    public static class SubCategoryResponseDto {
        private String code;
        private String name;
        private int level;
        private int order;

        @Builder
        public SubCategoryResponseDto(String code, String name, int level, int order) {
            this.code = code;
            this.name = name;
            this.level = level;
            this.order = order;
        }
    }
}
