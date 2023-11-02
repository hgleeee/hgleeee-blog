package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.PostResponseDto;
import com.hgleeee.blog.dto.PostUpdateRequestDto;

public interface PostService {

    PostResponseDto findById(Long id);
    Long saveEmptyPost();
    void update(PostUpdateRequestDto postUpdateRequestDto);
}
