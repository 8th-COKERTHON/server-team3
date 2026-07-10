package com.cotato.cokerthon.domain.roulette.controller;

import com.cotato.cokerthon.domain.roulette.dto.response.RouletteSliceResponse;
import com.cotato.cokerthon.domain.roulette.service.RouletteService;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
