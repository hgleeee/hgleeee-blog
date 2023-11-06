package com.hgleeee.blog.controller;

import com.hgleeee.blog.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
}
