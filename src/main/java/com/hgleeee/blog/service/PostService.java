package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.response.PostPreviewDto;
import com.hgleeee.blog.dto.response.PostResponseDto;
import com.hgleeee.blog.dto.request.PostUpdateRequestDto;
import com.hgleeee.blog.dto.SearchCriteriaDto;

import java.util.List;

public interface PostService {

    PostResponseDto findById(Long id);
    Long saveEmptyPost();
    void update(PostUpdateRequestDto postUpdateRequestDto);
    List<PostPreviewDto> getPostsBySearchCriteria(SearchCriteriaDto searchCriteriaDto);
}
