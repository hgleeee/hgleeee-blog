package com.hgleeee.blog.controller;

import com.hgleeee.blog.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
}
