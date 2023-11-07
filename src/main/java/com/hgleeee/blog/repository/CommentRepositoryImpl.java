package com.hgleeee.blog.repository;

import com.hgleeee.blog.domain.Comment;
import com.hgleeee.blog.dto.CommentCriteriaDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hgleeee.blog.domain.QComment.comment;
import static com.hgleeee.blog.domain.QPost.post;


@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findByCommentCriteria(CommentCriteriaDto commentCriteriaDto) {
        return queryFactory.selectFrom(comment)
                .join(comment.post, post)
                .where(commentCriteriaFit(commentCriteriaDto))
                .fetch();
//        return null;
    }

    private BooleanExpression commentCriteriaFit(CommentCriteriaDto commentCriteriaDto) {
        if (commentCriteriaDto.getPostId() != null) {
            return comment.post.id.eq(commentCriteriaDto.getPostId());
        }
        return null;
    }
}
