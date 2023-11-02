package com.hgleeee.blog.repository;

import com.hgleeee.blog.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
