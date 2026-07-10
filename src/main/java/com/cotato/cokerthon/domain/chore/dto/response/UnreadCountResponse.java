package com.cotato.cokerthon.domain.chore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UnreadCountResponse(

        @Schema(description = "읽지 않은 알림 수 (0이면 빨간 점 없음)", example = "3")
        long unreadCount
) {
}
