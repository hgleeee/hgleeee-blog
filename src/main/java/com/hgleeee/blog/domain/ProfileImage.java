package com.hgleeee.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
