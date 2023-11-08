package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.Comment;
import com.hgleeee.blog.dto.response.NoticeResponseDto;

import java.util.List;

public interface NoticeService {

    void createNotice(Comment comment);

    List<NoticeResponseDto> getNoticesByReceiverEmail(String email);
}
