package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.File;
import com.hgleeee.blog.domain.ProfileImage;
import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.dto.response.FileUrlResponseDto;
import com.hgleeee.blog.exception.LocalFileException;
import com.hgleeee.blog.exception.UserNotFoundException;
import com.hgleeee.blog.repository.FileRepository;
import com.hgleeee.blog.repository.ProfileImageRepository;
import com.hgleeee.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Profile("local")
public class LocalProfileImageService implements ProfileImageService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final ProfileImageRepository profileImageRepository;

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public FileUrlResponseDto uploadProfileImage(String email, MultipartFile file) {
        String fullPath = fileDir + file.getOriginalFilename();
        try {
            file.transferTo(new java.io.File(fullPath));
        } catch (IOException e) {
            throw new LocalFileException();
        }
        saveProfileImageDatabase(email, file, fullPath);
        return FileUrlResponseDto.builder()
                .fileUrl(fullPath)
                .build();
    }

    private void saveProfileImageDatabase(String email, MultipartFile file, String fullPath) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        File savedFile = fileRepository.save(File.builder()
                .url(fullPath)
                .name(file.getName())
                .build());
        profileImageRepository.save(ProfileImage.builder()
                .user(user)
                .file(savedFile)
                .build());
    }
}
