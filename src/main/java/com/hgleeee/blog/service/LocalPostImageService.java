package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.File;
import com.hgleeee.blog.domain.Post;
import com.hgleeee.blog.domain.PostImage;
import com.hgleeee.blog.dto.request.PostImageRequestDto;
import com.hgleeee.blog.dto.response.FileUrlResponseDto;
import com.hgleeee.blog.exception.LocalFileException;
import com.hgleeee.blog.exception.PostNotFoundException;
import com.hgleeee.blog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Profile("local")
@RequiredArgsConstructor
@Service
public class LocalPostImageService implements PostImageService {

    private final FileRepository fileRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public FileUrlResponseDto uploadPostImage(PostImageRequestDto postImageRequestDto, MultipartFile file) {
        String fullPath = fileDir + file.getOriginalFilename();
        try {
            file.transferTo(new java.io.File(fullPath));
        } catch (IOException e) {
            throw new LocalFileException();
        }
        savePostImageDatabase(postImageRequestDto, file, fullPath);
        return FileUrlResponseDto.builder()
                .fileUrl(fullPath)
                .build();
    }

    private void savePostImageDatabase(PostImageRequestDto postImageRequestDto,
                                       MultipartFile file, String fullPath) {
        Post post = postRepository.findById(postImageRequestDto.getPostId())
                .orElseThrow(PostNotFoundException::new);
        File savedFile = fileRepository.save(File.builder()
                .url(fullPath)
                .name(file.getName())
                .build());
        postImageRepository.save(PostImage.builder()
                .post(post)
                .file(savedFile)
                .build());
    }
}
