package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.Comment;
import com.hgleeee.blog.domain.Post;
import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.dto.CommentCriteriaDto;
import com.hgleeee.blog.dto.CommentRequestDto;
import com.hgleeee.blog.dto.CommentResponseDto;
import com.hgleeee.blog.dto.CommentUpdateRequestDto;
import com.hgleeee.blog.exception.CommentNotFoundException;
import com.hgleeee.blog.exception.NoAuthorityException;
import com.hgleeee.blog.exception.PostNotFoundException;
import com.hgleeee.blog.exception.UserNotFoundException;
import com.hgleeee.blog.repository.CommentRepository;
import com.hgleeee.blog.repository.PostRepository;
import com.hgleeee.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public Long register(Authentication authentication, CommentRequestDto commentRequestDto) {
        final User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        final Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(PostNotFoundException::new);
        if (commentRequestDto.getParentCommentId() == null) {
            return commentRepository.save(commentRequestDto.toEntity(user, post)).getId();
        }
        Comment parentComment = commentRepository.findById(commentRequestDto.getParentCommentId())
                .orElseThrow(CommentNotFoundException::new);

        Comment savedComment = commentRepository.save(
                commentRequestDto.toEntity(user, post, parentComment));
        return savedComment.getId();
    }

    @Override
    public List<CommentResponseDto> getComments(CommentCriteriaDto commentCriteriaDto) {
        int offset = (commentCriteriaDto.getPageNo()-1) * commentCriteriaDto.getPageSize();
        List<Comment> comments = commentRepository.findByCommentCriteria(commentCriteriaDto);
        Collections.sort(comments);

        return comments
                .subList(offset, offset + commentCriteriaDto.getPageSize())
                .stream()
                .map(c -> CommentResponseDto.builder()
                        .content(c.getContent())
                        .authorName(c.getUser().getName())
                        .level(c.getLevel())
                        .createdAt(c.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void update(Authentication authentication, CommentUpdateRequestDto commentUpdateRequestDto) {
        Comment comment = commentRepository.findById(commentUpdateRequestDto.getCommentId())
                .orElseThrow(CommentNotFoundException::new);
        if (!comment.getUser().getEmail().equals(authentication.getName())) {
            throw new NoAuthorityException();
        }
        comment.update(commentUpdateRequestDto.getContent());
    }

    @Override
    public void delete(Authentication authentication, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        if (!comment.getUser().getEmail().equals(authentication.getName())) {
            throw new NoAuthorityException();
        }
        comment.delete();
    }
}
