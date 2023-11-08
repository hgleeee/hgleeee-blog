package com.hgleeee.blog.controller;

import com.hgleeee.blog.dto.response.NoticeResponseDto;
import com.hgleeee.blog.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getNotices(Authentication authentication) {
        List<NoticeResponseDto> notices = noticeService.getNoticesByReceiverEmail(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body(notices);
    }
}
