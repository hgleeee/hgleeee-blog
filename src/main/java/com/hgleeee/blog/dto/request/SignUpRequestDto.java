package com.hgleeee.blog.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequestDto {

    @NotEmpty(message = "이름을 입력해주세요")
    private String name;

    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    @Size(min = 8, max = 16, message = "비밀번호는 8자리 이상 16자리 이하로 설정해주세요.")
    private String password;

    @Builder
    public SignUpRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
