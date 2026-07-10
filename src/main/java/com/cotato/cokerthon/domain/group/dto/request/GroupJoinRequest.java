package com.cotato.cokerthon.domain.group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record GroupJoinRequest(
        @Schema(description = "초대 코드", example = "A1B2C3D4")
        @NotBlank(message = "초대 코드는 필수입니다.")
        String inviteCode
) {
}
