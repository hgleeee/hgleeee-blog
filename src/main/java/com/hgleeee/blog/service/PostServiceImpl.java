package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.Category;
import com.hgleeee.blog.domain.Post;
import com.hgleeee.blog.dto.response.PostPreviewDto;
import com.hgleeee.blog.dto.response.PostResponseDto;
import com.hgleeee.blog.dto.request.PostUpdateRequestDto;
import com.hgleeee.blog.dto.SearchCriteriaDto;
import com.hgleeee.blog.exception.CategoryNotFoundException;
import com.hgleeee.blog.exception.PostNotFoundException;
import com.hgleeee.blog.repository.CategoryRepository;
import com.hgleeee.blog.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public PostResponseDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        return post.createPostResponseDto();
    }

    @Override
    public Long saveEmptyPost() {
        Post post = Post.builder()
                .title("")
                .content("")
                .build();

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Override
    public void update(PostUpdateRequestDto postUpdateRequestDto) {
        Post post = postRepository.findById(postUpdateRequestDto.getPostId())
                .orElseThrow(PostNotFoundException::new);
        post.update(postUpdateRequestDto);

        Category category = categoryRepository.findById(postUpdateRequestDto.getCategoryCode())
                .orElseThrow(CategoryNotFoundException::new);
        post.setCategory(category);
    }

    @Override
    public List<PostPreviewDto> getPostsBySearchCriteria(SearchCriteriaDto searchCriteriaDto) {
        return postRepository.getPostsBySearchCriteria(searchCriteriaDto)
                .stream().map(p -> PostPreviewDto.builder()
                        .title(p.getTitle())
                        .authorName(p.getUser().getName())
                        .createdAt(p.getCreatedAt())
                        .viewCount(p.getViewCount())
                        .build())
                .collect(Collectors.toList());
    }
}
