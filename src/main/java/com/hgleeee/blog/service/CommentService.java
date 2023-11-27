package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.CommentCriteriaDto;
import com.hgleeee.blog.dto.request.CommentRequestDto;
import com.hgleeee.blog.dto.request.CommentUpdateRequestDto;
import com.hgleeee.blog.dto.response.CommentsResponseDto;

public interface CommentService {
    Long register(String currentUserEmail, CommentRequestDto commentRequestDto);

    CommentsResponseDto getComments(CommentCriteriaDto commentCriteriaDto);

    void delete(String currentUserEmail, Long commentId);

    void update(String currentUserEmail, CommentUpdateRequestDto commentUpdateRequestDto);
}
