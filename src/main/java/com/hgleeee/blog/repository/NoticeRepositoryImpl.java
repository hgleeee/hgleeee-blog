package com.hgleeee.blog.repository;

import com.hgleeee.blog.domain.Notice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hgleeee.blog.domain.QNotice.notice;
import static com.hgleeee.blog.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notice> getNoticesByReceiverEmail(String email) {
        return queryFactory
                .selectFrom(notice)
                .join(notice.receiver, user)
                .where(notice.receiver.email.eq(email))
                .orderBy(notice.createdAt.desc())
                .fetch();
    }
}
