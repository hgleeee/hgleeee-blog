package com.hgleeee.blog.repository;

import com.hgleeee.blog.domain.Post;
import com.hgleeee.blog.dto.SearchCriteriaDto;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getPostsBySearchCriteria(SearchCriteriaDto searchCriteriaDto);
}
