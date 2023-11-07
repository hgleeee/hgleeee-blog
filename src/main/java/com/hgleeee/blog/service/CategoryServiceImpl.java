package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.Category;
import com.hgleeee.blog.dto.request.CategoryRequestDto;
import com.hgleeee.blog.dto.response.CategoryResponseDto;
import com.hgleeee.blog.exception.CategoryNotFoundException;
import com.hgleeee.blog.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        Collections.sort(categories);
        return categories.stream()
                .map(c -> CategoryResponseDto.toDto(c))
                .collect(Collectors.toList());
    }

    @Override
    public void update(CategoryRequestDto categoryRequestDto) {
        List<Category> categories = categoryRepository.findAll();
        for (CategoryRequestDto.CategoryUnitRequestDto categoryUnitRequestDto : categoryRequestDto.getCategories()) {
            Category category = extractCategory(categoryUnitRequestDto, categories);
            if (category != null) {
                category.update(categoryUnitRequestDto);
                continue;
            }

            Category toSaveCategory = categoryUnitRequestDto.toEntity();
            toSaveCategory.setParentCategory(
                    categoryRepository.findById(categoryUnitRequestDto.getCode())
                            .orElseThrow(CategoryNotFoundException::new));
            categoryRepository.save(toSaveCategory);
        }
    }

    private Category extractCategory(CategoryRequestDto.CategoryUnitRequestDto categoryUnitRequestDto,
                                     List<Category> categories) {
        for (Category category : categories) {
            if (category.getCode().equals(categoryUnitRequestDto.getCode())) {
                return category;
            }
        }
        return null;
    }
}
