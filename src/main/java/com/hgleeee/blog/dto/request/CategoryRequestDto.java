package com.hgleeee.blog.dto.request;

import com.hgleeee.blog.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    private List<CategoryUnitRequestDto> categories;

    @Data
    @NoArgsConstructor
    public static class CategoryUnitRequestDto {

        private String code;
        private String parentCode;
        private String name;
        private int level;
        private int order;

        @Builder
        public CategoryUnitRequestDto(String code, String parentCode, String name, int level, int order) {
            this.code = code;
            this.parentCode = parentCode;
            this.name = name;
            this.order = order;
            this.level = level;
        }

        public Category toEntity() {
            return Category.builder()
                    .code(code)
                    .level(level)
                    .order(order)
                    .name(name)
                    .build();
        }
    }
}
