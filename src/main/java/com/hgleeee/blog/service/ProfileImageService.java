package com.hgleeee.blog.service;

import com.hgleeee.blog.dto.response.FileUrlResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {

    FileUrlResponseDto uploadProfileImage(String email, MultipartFile file);
}
