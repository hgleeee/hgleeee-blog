package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.request.CategoryRequestDto;
import com.hgleeee.blog.dto.response.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll();

    void update(CategoryRequestDto categoryRequestDto);
}
