package com.hgleeee.blog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private LocalDateTime deletedAt;

    @OneToOne(mappedBy = "user")
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "receiver")
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public User(String name, String email, String password) {
        this.role = Role.USER;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
