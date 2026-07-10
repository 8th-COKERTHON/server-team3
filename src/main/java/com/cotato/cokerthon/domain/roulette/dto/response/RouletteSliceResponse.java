package com.cotato.cokerthon.domain.roulette.dto.response;

public record RouletteSliceResponse(
        Long memberId,
        double shareRatio  // 룰렛 판에서 이 멤버가 차지하는 칸 비율 (0~100)
) {
}
