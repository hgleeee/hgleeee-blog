package com.hgleeee.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenRequestDto {

    private String accessToken;
    private String refreshToken;

}
