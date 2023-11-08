package com.hgleeee.blog.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileUrlResponseDto {

    private String fileUrl;

    @Builder
    public FileUrlResponseDto(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
