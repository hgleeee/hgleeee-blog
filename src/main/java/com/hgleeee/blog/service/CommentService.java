package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.CommentCriteriaDto;
import com.hgleeee.blog.dto.CommentRequestDto;
import com.hgleeee.blog.dto.CommentResponseDto;
import com.hgleeee.blog.dto.CommentUpdateRequestDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService {
    Long register(Authentication authentication, CommentRequestDto commentRequestDto);

    List<CommentResponseDto> getComments(CommentCriteriaDto commentCriteriaDto);

    void delete(Authentication authentication, Long commentId);

    void update(Authentication authentication, CommentUpdateRequestDto commentUpdateRequestDto);
}
