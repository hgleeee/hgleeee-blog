package com.hgleeee.blog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category {

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

    @Builder
    public Category(String code, String name, Integer level) {
        this.code = code;
        this.name = name;
        this.level = level;
    }
}
