package com.cotato.cokerthon.domain.chore.dto.request;

import com.cotato.cokerthon.domain.chore.entity.ChoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GroupChoreStatusUpdateRequest(
        @Schema(description = "변경할 진행 단계", example = "IN_PROGRESS")
        @NotNull(message = "진행 단계는 필수입니다.")
        ChoreStatus status,

        @Schema(description = "실제로 집안일을 수행한 사람 ID (완료 처리 시에만 사용. 생략하면 담당자가 직접 수행한 것으로 처리. 담당자와 다르면 담당자는 5점 감점되고 수행자가 집안일 점수만큼 기여도를 얻음)")
        Long performerId
) {
}