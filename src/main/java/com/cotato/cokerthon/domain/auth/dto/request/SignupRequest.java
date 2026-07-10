package com.cotato.cokerthon.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(

        @Schema(description = "이름", example = "이해원")
        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @Schema(description = "로그인 아이디", example = "test")
        @NotBlank(message = "아이디는 필수입니다.")
        String loginId,

        @Schema(description = "비밀번호 (8자 이상, 영문/숫자 포함)", example = "password1")
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$",
                message = "비밀번호는 8자 이상이어야 하며, 영문과 숫자를 모두 포함해야 합니다."
        )
        String password,

        @Schema(description = "비밀번호 확인", example = "password1")
        @NotBlank(message = "비밀번호 확인은 필수입니다.")
        String passwordCheck
) {
}
