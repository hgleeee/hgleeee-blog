package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.request.PostImageRequestDto;
import com.hgleeee.blog.dto.response.FileUrlResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface PostImageService {

    FileUrlResponseDto uploadPostImage(PostImageRequestDto postImageRequestDto, MultipartFile file);
}
