package com.hgleeee.blog.repository;

import com.hgleeee.blog.domain.Post;
import com.hgleeee.blog.dto.SearchCriteriaDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hgleeee.blog.domain.QCategory.category;
import static com.hgleeee.blog.domain.QPost.post;
import static com.hgleeee.blog.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> getPostsBySearchCriteria(SearchCriteriaDto searchCriteriaDto) {
        long offset = (searchCriteriaDto.getPageNo() - 1) * searchCriteriaDto.getPageSize();
        return queryFactory.selectFrom(post)
                .join(post.user, user)
                .join(post.category, category)
                .where(searchCriteriaFit(searchCriteriaDto))
                .orderBy(post.createdAt.desc())
                .limit(searchCriteriaDto.getPageSize())
                .offset(offset)
                .fetch();
    }

    private BooleanExpression searchCriteriaFit(SearchCriteriaDto searchCriteriaDto) {
        String categoryCode = searchCriteriaDto.getCategory();
        if (StringUtils.isEmpty(categoryCode)) {
            return null;
        }
        return post.category.code.eq(categoryCode);
    }
}
