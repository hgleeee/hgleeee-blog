package com.hgleeee.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
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
}
