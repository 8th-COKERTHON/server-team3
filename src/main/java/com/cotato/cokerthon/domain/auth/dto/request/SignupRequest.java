package com.cotato.cokerthon.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(

        @Schema(description = "이름", example = "이해원")
        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @Schema(description = "로그인 아이디", example = "test")
        @NotBlank(message = "아이디는 필수입니다.")
        String loginId,

        @Schema(description = "비밀번호", example = "password")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
}
