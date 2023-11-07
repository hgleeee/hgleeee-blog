package com.hgleeee.blog.repository;

import com.hgleeee.blog.domain.Comment;
import com.hgleeee.blog.dto.CommentCriteriaDto;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findByCommentCriteria(CommentCriteriaDto commentCriteriaDto);
}
