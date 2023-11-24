package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.Category;
import com.hgleeee.blog.dto.request.CategoryRequestDto;
import com.hgleeee.blog.dto.response.CategoryResponseDto;
import com.hgleeee.blog.exception.CategoryNotFoundException;
import com.hgleeee.blog.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .filter(c -> c.getLevel() == 0)
                .map(c -> CategoryResponseDto.toDto(c))
                .collect(Collectors.toList());
    }

    @Override
    public void update(CategoryRequestDto categoryRequestDto) {
        List<String> savedCodes = categoryRepository.findAll().stream()
                .map(c -> c.getCode())
                .collect(Collectors.toList());
        for (CategoryRequestDto.CategoryUnitRequestDto categoryUnitRequestDto : categoryRequestDto.getCategories()) {
            updateMainCategory(categoryUnitRequestDto);
            savedCodes.remove(categoryUnitRequestDto.getCode());
        }
        for (CategoryRequestDto.CategoryUnitRequestDto categoryUnitRequestDto : categoryRequestDto.getCategories()) {
            for (CategoryRequestDto.SubCategoryUnitRequestDto subCategoryUnitRequestDto : categoryUnitRequestDto.getSubCategories()) {
                updateSubCategory(subCategoryUnitRequestDto, categoryUnitRequestDto.getCode());
                savedCodes.remove(subCategoryUnitRequestDto.getCode());
            }
        }

        categoryRepository.deleteAllByIdInBatch(savedCodes);
    }

    private void updateSubCategory(CategoryRequestDto.SubCategoryUnitRequestDto subCategoryUnitRequestDto,
                                   String parentCode) {
        Optional<Category> optionalCategory = categoryRepository.findById(subCategoryUnitRequestDto.getCode());
        if (optionalCategory.isPresent()) {
            optionalCategory.get().update(subCategoryUnitRequestDto);
            return;
        }
        categoryRepository.save(subCategoryUnitRequestDto.toEntity(
                categoryRepository.findById(parentCode).orElseThrow(CategoryNotFoundException::new)));
    }

    private void updateMainCategory(CategoryRequestDto.CategoryUnitRequestDto categoryUnitRequestDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryUnitRequestDto.getCode());
        if (optionalCategory.isPresent()) {
            optionalCategory.get().update(categoryUnitRequestDto);
            return;
        }
        categoryRepository.save(categoryUnitRequestDto.toEntity());
    }
}
