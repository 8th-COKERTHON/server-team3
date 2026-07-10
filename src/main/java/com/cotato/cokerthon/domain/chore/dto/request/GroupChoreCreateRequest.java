package com.cotato.cokerthon.domain.chore.dto.request;

import com.cotato.cokerthon.domain.chore.entity.AssignType;
import com.cotato.cokerthon.domain.chore.entity.Difficulty;
import com.cotato.cokerthon.domain.chore.entity.RepeatCycle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GroupChoreCreateRequest(
        @Schema(description = "제목", example = "설거지하기")
        @NotBlank(message = "제목은 필수입니다.")
        String name,

        @Schema(description = "난이도")
        @NotNull(message = "난이도는 필수입니다.")
        Difficulty difficulty,

        @Schema(description = "날짜(반복인 경우 시작 날짜)", example = "2026-07-11")
        @NotNull(message = "날짜는 필수입니다.")
        LocalDate date,

        @Schema(description = "담당자 지정 방식", example = "MANUAL")
        @NotNull(message = "담당자 지정 방식은 필수입니다.")
        AssignType assignType,

        @Schema(description = "담당자 ID (assignType이 MANUAL일 때만 사용)")
        Long assigneeId,

        @Schema(description = "반복 주기", example = "WEEKLY")
        @NotNull(message = "반복 주기는 필수입니다.")
        RepeatCycle repeatCycle,

        @Schema(description = "반복 세부 패턴 (매주/격주: 요일 목록 예) \"MON,WED,FRI\", 매월/매년: 캘린더로 선택한 날짜 목록 예) \"2026-07-15\")")
        String repeatPattern,

        @Schema(description = "메모")
        String memo
) {
}
