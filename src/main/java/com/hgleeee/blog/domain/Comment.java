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
public class Comment extends BaseTimeEntity implements Comparable<Comment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private Integer level;

    private LocalDateTime deletedAt;

    @Builder
    public Comment(String content, Post post, User user, Comment parentComment, Integer level) {
        this.content = content;
        this.post = post;
        this.user = user;
        this.parentComment = parentComment;
        this.level = level;
        if (level == null) {
            this.level = 0;
        }
    }

    @Override
    public int compareTo(Comment o) {
        Comment originComment = this, toCompareComment = o;
        while (originComment.level > toCompareComment.level) {
            originComment = originComment.parentComment;
        }
        while (originComment.level < toCompareComment.level) {
            toCompareComment = toCompareComment.parentComment;
        }
        if (originComment.id == toCompareComment.id) {
            if (level != o.level) {
                return level - o.level;
            }
            return getCreatedAt().compareTo(o.getCreatedAt());
        }
        return originComment.getCreatedAt().compareTo(toCompareComment.getCreatedAt());
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
