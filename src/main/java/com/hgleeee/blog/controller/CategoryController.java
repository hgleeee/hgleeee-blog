package com.hgleeee.blog.controller;

import com.hgleeee.blog.dto.request.CategoryRequestDto;
import com.hgleeee.blog.dto.response.CategoryResponseDto;
import com.hgleeee.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        return ResponseEntity.status(OK).body(categoryService.findAll());
    }

    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.update(categoryRequestDto);
        return ResponseEntity.status(OK).build();
    }

}
