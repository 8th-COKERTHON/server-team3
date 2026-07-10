package com.cotato.cokerthon.domain.chore.controller;

import com.cotato.cokerthon.domain.chore.dto.response.ChoreResponse;
import com.cotato.cokerthon.domain.chore.service.ChoreService;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chores")
@RequiredArgsConstructor
@Tag(name = "Chore API")
public class ChoreController {

    private final ChoreService choreService;

    @Operation(summary = "기본 집안일 목록 조회 API", description = "미리 정의된 기본 집안일(카탈로그) 목록을 조회합니다.")
    @GetMapping
    public CommonResponse<List<ChoreResponse>> getAllChores() {
        List<ChoreResponse> response = choreService.getAllChores();
        return CommonResponse.success(response);
    }
}