package com.cotato.cokerthon.domain.chore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChoreRequestCreateRequest(

        @Schema(description = "요청받을 멤버 ID", example = "2")
        @NotNull(message = "수신자는 필수입니다.")
        Long receiverId,

        @Schema(description = "집안일 ID", example = "1")
        @NotNull(message = "집안일 ID는 필수입니다.")
        Long choreId,

        @Schema(description = "집안일 이름", example = "설거지")
        @NotBlank(message = "집안일 이름은 필수입니다.")
        String choreName
) {
}
