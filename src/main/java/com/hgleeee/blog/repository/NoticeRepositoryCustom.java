package com.hgleeee.blog.repository;

import com.hgleeee.blog.domain.Notice;

import java.util.List;

public interface NoticeRepositoryCustom {

    List<Notice> getNoticesByReceiverEmail(String email);
}
