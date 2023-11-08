package com.hgleeee.blog.controller;

import com.hgleeee.blog.dto.request.PostImageRequestDto;
import com.hgleeee.blog.dto.response.FileUrlResponseDto;
import com.hgleeee.blog.service.PostImageService;
import com.hgleeee.blog.service.ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileController {

    private final ProfileImageService profileImageService;
    private final PostImageService postImageService;

    @PostMapping("/profile-image/upload")
    public ResponseEntity<FileUrlResponseDto> uploadProfileImage(Authentication authentication,
                                                                 @RequestPart("image") MultipartFile file) {
        FileUrlResponseDto fileUrlResponseDto = profileImageService.uploadProfileImage(authentication.getName(), file);
        return ResponseEntity.status(HttpStatus.OK).body(fileUrlResponseDto);
    }

    @PostMapping("/post-image/upload")
    public ResponseEntity<FileUrlResponseDto> uploadPostImage(@RequestBody PostImageRequestDto postImageRequestDto,
                                                              @RequestPart("image") MultipartFile file) {
        FileUrlResponseDto fileUrlResponseDto = postImageService.uploadPostImage(postImageRequestDto, file);
        return ResponseEntity.status(HttpStatus.OK).body(fileUrlResponseDto);
    }
}
