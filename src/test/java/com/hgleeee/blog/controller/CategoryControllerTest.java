package com.hgleeee.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgleeee.blog.domain.Category;
import com.hgleeee.blog.dto.request.CategoryRequestDto;
import com.hgleeee.blog.exception.CategoryNotFoundException;
import com.hgleeee.blog.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    @AfterEach
    void init() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 가져오기 성공 - 서브 카테고리 없는 버전")
    void getCategories() throws Exception {
        categoryRepository.saveAll(IntStream.range(100, 110)
                .mapToObj(i -> Category.builder()
                        .name("카테고리 " + i)
                        .level(0)
                        .order(i - 100)
                        .code(String.valueOf(i))
                        .build())
                .collect(Collectors.toList()));

        mockMvc.perform(get("/api/category/categories"))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(10, categoryRepository.count());
    }

    @Test
    @DisplayName("카테고리 가져오기 성공 - 서브 카테고리 있는 버전")
    void getCategoriesWithSub() throws Exception {
        List<Category> categories = categoryRepository.saveAll(IntStream.range(100, 105)
                .mapToObj(i -> Category.builder()
                        .name("카테고리" + i)
                        .level(0)
                        .order(i - 100)
                        .code(String.valueOf(i))
                        .build())
                .collect(Collectors.toList()));
        for (int j = 100; j < 105; ++j) {
            final int idx = j;
            categoryRepository.saveAll(
                    IntStream.range(idx * 10, idx * 10 + 3)
                            .mapToObj(i -> Category.builder()
                                    .name("서브 카테고리" + i)
                                    .level(1)
                                    .parentCategory(categories.get(idx - 100))
                                    .code(String.valueOf(i))
                                    .order(idx * 10)
                                    .build())
                            .collect(Collectors.toList()));
        }

        mockMvc.perform(get("/api/category/categories"))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(20, categoryRepository.count());
    }

    @Test
    @DisplayName("카테고리 업데이트 성공 - 임시")
    void update() throws Exception {
        // given
        List<Category> categories = categoryRepository.saveAll(IntStream.range(100, 105)
                .mapToObj(i -> Category.builder()
                        .name("카테고리" + i)
                        .level(0)
                        .order(i - 100)
                        .code(String.valueOf(i))
                        .build())
                .collect(Collectors.toList()));
        for (int j = 100; j < 105; ++j) {
            final int idx = j;
            categoryRepository.saveAll(
                    IntStream.range(idx * 10, idx * 10 + 3)
                            .mapToObj(i -> Category.builder()
                                    .name("서브 카테고리" + i)
                                    .level(1)
                                    .parentCategory(categories.get(idx - 100))
                                    .code(String.valueOf(i))
                                    .order(idx * 10)
                                    .build())
                            .collect(Collectors.toList()));
        }

        List<CategoryRequestDto.CategoryUnitRequestDto> categoryUnitRequestDtoList = categoryRepository.findAll()
                .stream()
                .map(c -> CategoryRequestDto.CategoryUnitRequestDto.builder()
                        .code(c.getCode())
                        .order(c.getOrder())
                        .name(c.getName() + "0")
                        .level(c.getLevel())
                        .build())
                .collect(Collectors.toList());

        String json = objectMapper.writeValueAsString(new CategoryRequestDto(categoryUnitRequestDtoList));

        // when - then
        mockMvc.perform(post("/api/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        for (int i = 100; i < 105; ++i) {
            Category category = categoryRepository.findById(String.valueOf(i))
                    .orElseThrow(CategoryNotFoundException::new);
            Assertions.assertEquals("카테고리" + i + "0", category.getName());
            for (int j = 0; j < 3; ++j) {
                Category subCategory = categoryRepository.findById(String.valueOf(i * 10 + j))
                        .orElseThrow(CategoryNotFoundException::new);
                Assertions.assertEquals("서브 카테고리" + (i * 10 + j) + "0", subCategory.getName());
            }
        }
    }
}