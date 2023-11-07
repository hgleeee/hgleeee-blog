package com.hgleeee.blog.domain;

import com.hgleeee.blog.dto.request.CategoryRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category implements Comparable<Category> {

    @Id
    private String code;

    @JoinColumn(name = "parent_code")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> categories;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private Integer order;

    @Builder
    public Category(String code, String name, Integer level, Integer order, Category parentCategory) {
        this.code = code;
        this.name = name;
        this.level = level;
        this.order = order;
        this.parentCategory = parentCategory;
    }

    @Override
    public int compareTo(Category o) {
        Category origin = this, toCompare = o;
        while (origin.level > 0) {
            origin = origin.parentCategory;
        }
        while (toCompare.level > 0) {
            toCompare = toCompare.parentCategory;
        }
        if (origin.order != toCompare.order) {
            return origin.order - toCompare.order;
        }
        return order - o.order;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public void update(CategoryRequestDto.CategoryUnitRequestDto categoryUnitRequestDto) {
        this.code = categoryUnitRequestDto.getCode();
        this.name = categoryUnitRequestDto.getName();
    }
}
