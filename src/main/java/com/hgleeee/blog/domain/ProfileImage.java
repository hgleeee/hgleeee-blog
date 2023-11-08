package com.hgleeee.blog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public ProfileImage(File file, User user) {
        this.file = file;
        this.user = user;
    }
}
