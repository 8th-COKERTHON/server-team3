package com.cotato.cokerthon.domain.chore.dto.request;

import com.cotato.cokerthon.domain.chore.entity.ChoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GroupChoreStatusUpdateRequest(
        @Schema(description = "변경할 진행 단계", example = "IN_PROGRESS")
        @NotNull(message = "진행 단계는 필수입니다.")
        ChoreStatus status
) {
}