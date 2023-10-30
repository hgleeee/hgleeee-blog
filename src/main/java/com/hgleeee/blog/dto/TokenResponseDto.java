package com.hgleeee.blog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenResponseDto {

    private String accessToken;

    @Builder
    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
