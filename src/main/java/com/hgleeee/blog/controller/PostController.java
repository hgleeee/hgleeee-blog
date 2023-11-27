package com.hgleeee.blog.controller;

import com.hgleeee.blog.dto.SearchCriteriaDto;
import com.hgleeee.blog.dto.request.PostUpdateRequestDto;
import com.hgleeee.blog.dto.response.PostResponseDto;
import com.hgleeee.blog.dto.response.PostsPreviewDto;
import com.hgleeee.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post Controller", description = "Post 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;


    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponse = postService.findById(postId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(postResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register() {
        Long postId = postService.saveEmptyPost();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postId);
    }

    @Operation(summary = "글 업데이트", description = "요청 값 검증 후 글 업데이트")
    @PatchMapping("/update")
    public ResponseEntity<Long> update(final @Valid @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        postService.update(postUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(postUpdateRequestDto.getPostId());
    }

    @GetMapping("/posts")
    public ResponseEntity<PostsPreviewDto> getPosts(@ModelAttribute SearchCriteriaDto searchCriteriaDto) {
        PostsPreviewDto postsPreviewDto = postService.getPostsBySearchCriteria(searchCriteriaDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(postsPreviewDto);
    }
}
