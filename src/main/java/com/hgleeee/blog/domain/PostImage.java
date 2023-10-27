package com.hgleeee.blog.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "file_id")
    @OneToOne
    private File file;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
