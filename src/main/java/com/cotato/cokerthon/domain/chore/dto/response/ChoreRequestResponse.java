package com.cotato.cokerthon.domain.chore.dto.response;

import com.cotato.cokerthon.domain.chore.entity.ChoreRequest;
import com.cotato.cokerthon.domain.chore.entity.ChoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ChoreRequestResponse(

        @Schema(description = "요청 ID", example = "1")
        Long requestId,

        @Schema(description = "보낸 사람 이름", example = "김코코")
        String senderName,

        @Schema(description = "집안일 ID", example = "1")
        Long choreId,

        @Schema(description = "알림 메시지", example = "설거지 부탁했어요")
        String message,

        @Schema(description = "요청 상태", example = "PENDING")
        ChoreStatus status,

        @Schema(description = "요청 생성 시각")
        LocalDateTime createdAt
) {
    public static ChoreRequestResponse fromReceiverView(ChoreRequest choreRequest) {
        return new ChoreRequestResponse(
                choreRequest.getId(),
                choreRequest.getSender().getName(),
                choreRequest.getChoreId(),
                choreRequest.getChoreName() + " 부탁했어요",
                choreRequest.getStatus(),
                choreRequest.getCreatedAt()
        );
    }

    public static ChoreRequestResponse fromSenderView(ChoreRequest choreRequest) {
        return new ChoreRequestResponse(
                choreRequest.getId(),
                choreRequest.getSender().getName(),
                choreRequest.getChoreId(),
                choreRequest.getChoreName() + " 완료했어요",
                choreRequest.getStatus(),
                choreRequest.getCreatedAt()
        );
    }
}
