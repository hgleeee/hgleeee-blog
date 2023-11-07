package com.hgleeee.blog.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenRequestDto {

    private String accessToken;
    private String refreshToken;

}
