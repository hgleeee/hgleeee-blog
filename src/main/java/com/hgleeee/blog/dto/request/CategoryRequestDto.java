package com.hgleeee.blog.dto.request;

import com.hgleeee.blog.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    private List<CategoryUnitRequestDto> categories;

    @Data
    @NoArgsConstructor
    public static class CategoryUnitRequestDto {

        private String code;
        private String name;
        private int level;
        private int order;
        private List<SubCategoryUnitRequestDto> subCategories;

        @Builder
        public CategoryUnitRequestDto(String code, String name, int level, int order) {
            this.code = code;
            this.name = name;
            this.order = order;
            this.level = level;
        }

        public Category toEntity() {
            Category category = Category.builder()
                    .code(code)
                    .level(level)
                    .order(order)
                    .name(name)
                    .build();
            category.setCategories(subCategories.stream()
                            .map(s -> s.toEntity(category))
                            .collect(Collectors.toList()));
            return category;
        }
    }

    @Data
    @NoArgsConstructor
    public static class SubCategoryUnitRequestDto {

        private String code;
        private String name;
        private int level;
        private int order;

        @Builder
        public SubCategoryUnitRequestDto(String code, String name, int level, int order) {
            this.code = code;
            this.name = name;
            this.order = order;
            this.level = level;
        }

        public Category toEntity(Category parentCategory) {
            return Category.builder()
                    .code(code)
                    .level(level)
                    .order(order)
                    .name(name)
                    .parentCategory(parentCategory)
                    .build();
        }
    }
}
