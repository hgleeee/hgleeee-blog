package com.hgleeee.blog.controller;

import com.hgleeee.blog.dto.PostResponseDto;
import com.hgleeee.blog.dto.PostUpdateRequestDto;
import com.hgleeee.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponse = postService.findById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register() {
        Long postId = postService.saveEmptyPost();
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @PatchMapping("/update")
    public ResponseEntity<Long> update(final @Valid @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        postService.update(postUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(postUpdateRequestDto.getPostId());
    }
}
