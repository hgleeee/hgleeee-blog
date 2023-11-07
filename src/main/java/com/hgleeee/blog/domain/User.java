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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void registerProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

}
