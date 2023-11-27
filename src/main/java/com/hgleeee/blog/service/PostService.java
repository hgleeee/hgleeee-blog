package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.SearchCriteriaDto;
import com.hgleeee.blog.dto.request.PostUpdateRequestDto;
import com.hgleeee.blog.dto.response.PostResponseDto;
import com.hgleeee.blog.dto.response.PostsPreviewDto;

public interface PostService {

    PostResponseDto findById(Long id);
    Long saveEmptyPost();
    void update(PostUpdateRequestDto postUpdateRequestDto);
    PostsPreviewDto getPostsBySearchCriteria(SearchCriteriaDto searchCriteriaDto);
}
