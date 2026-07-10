package com.cotato.cokerthon.domain.group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record GroupCreateRequest(
        @Schema(description = "그룹 이름", example = "우리 가족")
        @NotBlank(message = "그룹 이름은 필수입니다.")
        String groupName
) {
}
