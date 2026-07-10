package com.cotato.cokerthon.domain.roulette.controller;

import com.cotato.cokerthon.domain.roulette.dto.response.RouletteResultResponse;
import com.cotato.cokerthon.domain.roulette.dto.response.RouletteSliceResponse;
import com.cotato.cokerthon.domain.roulette.service.RouletteService;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Roulette", description = "룰렛 API")
@RestController
@RequestMapping("/api/groups/{groupId}/roulette")
@RequiredArgsConstructor
public class RouletteController {

    private final RouletteService rouletteService;

    @Operation(summary = "룰렛 지분 조회", description = "그룹 멤버별 totalPoints 기반으로 룰렛 칸 비율을 반환합니다.")
    @GetMapping("/slices")
    public CommonResponse<List<RouletteSliceResponse>> getSlices(@PathVariable Long groupId) {
        return CommonResponse.success(rouletteService.getSlices(groupId));
    }

    @Operation(summary = "룰렛 결과 조회", description = "룰렛으로 배정된 집안일 이력을 반환합니다.")
    @GetMapping("/results")
    public CommonResponse<List<RouletteResultResponse>> getResults(
            @PathVariable Long groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nextWeekStartDate
    ) {
        return CommonResponse.success(rouletteService.getResults(groupId, nextWeekStartDate));
    }
}
