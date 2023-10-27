package com.hgleeee.blog.controller;

import com.hgleeee.blog.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
}
