package com.hgleeee.blog.controller;

import com.hgleeee.blog.dto.CommentCriteriaDto;
import com.hgleeee.blog.dto.request.CommentRequestDto;
import com.hgleeee.blog.dto.request.CommentUpdateRequestDto;
import com.hgleeee.blog.dto.response.CommentsResponseDto;
import com.hgleeee.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<CommentsResponseDto> getComments(final @ModelAttribute CommentCriteriaDto commentCriteriaDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getComments(commentCriteriaDto));
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(Authentication authentication,
                                         final @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.register(authentication.getName(), commentRequestDto));
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> update(Authentication authentication,
                                       @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) {
        commentService.update(authentication.getName(), commentUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(Authentication authentication,
                                       @RequestParam Long commentId) {
        commentService.delete(authentication.getName(), commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
