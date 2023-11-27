package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.Comment;
import com.hgleeee.blog.domain.Post;
import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.dto.CommentCriteriaDto;
import com.hgleeee.blog.dto.request.CommentRequestDto;
import com.hgleeee.blog.dto.request.CommentUpdateRequestDto;
import com.hgleeee.blog.dto.response.CommentResponseDto;
import com.hgleeee.blog.dto.response.CommentsResponseDto;
import com.hgleeee.blog.exception.*;
import com.hgleeee.blog.repository.CommentRepository;
import com.hgleeee.blog.repository.PostRepository;
import com.hgleeee.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final NoticeService noticeService;

    @Override
    public Long register(String currentUserEmail, CommentRequestDto commentRequestDto) {
        final User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(UserNotFoundException::new);
        final Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(PostNotFoundException::new);
        Comment savedComment;
        if (commentRequestDto.getParentCommentId() == null) {
            savedComment = commentRepository.save(commentRequestDto.toEntity(user, post));
        } else {
            Comment parentComment = commentRepository.findById(commentRequestDto.getParentCommentId())
                    .orElseThrow(CommentNotFoundException::new);
            savedComment = commentRepository.save(commentRequestDto.toEntity(user, post, parentComment));
        }
        noticeService.createNotice(savedComment);
        return savedComment.getId();
    }

    @Override
    public CommentsResponseDto getComments(CommentCriteriaDto commentCriteriaDto) {
        int offset = (commentCriteriaDto.getPageNo() - 1) * commentCriteriaDto.getPageSize();
        List<Comment> comments = commentRepository.findByCommentCriteria(commentCriteriaDto);
        if (pageNumberNotValid(comments, offset)) {
            throw new PageNotFoundException();
        }
        Collections.sort(comments);

        return CommentsResponseDto.builder()
                .totalCommentCount(comments.size())
                .comments(comments
                        .subList(offset, Math.min(offset + commentCriteriaDto.getPageSize(), comments.size()))
                        .stream()
                        .map(c -> CommentResponseDto.builder()
                                .id(c.getId())
                                .content(c.getContent())
                                .authorName(c.getUser().getName())
                                .level(c.getLevel())
                                .createdAt(c.getCreatedAt())
                                .deletedAt(c.getDeletedAt())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private static boolean pageNumberNotValid(List<Comment> comments, int offset) {
        return comments.size() < offset;
    }

    @Override
    public void update(String currentUserEmail, CommentUpdateRequestDto commentUpdateRequestDto) {
        Comment comment = commentRepository.findById(commentUpdateRequestDto.getCommentId())
                .orElseThrow(CommentNotFoundException::new);
        if (!comment.getUser().getEmail().equals(currentUserEmail)) {
            throw new NoAuthorityException();
        }
        comment.update(commentUpdateRequestDto.getContent());
    }

    @Override
    public void delete(String currentUserEmail, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        if (!comment.getUser().getEmail().equals(currentUserEmail)) {
            throw new NoAuthorityException();
        }
        comment.delete();
    }
}
