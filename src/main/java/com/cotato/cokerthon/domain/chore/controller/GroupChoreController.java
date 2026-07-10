package com.cotato.cokerthon.domain.chore.controller;

import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreCreateRequest;
import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreFromCatalogRequest;
import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreStatusUpdateRequest;
import com.cotato.cokerthon.domain.chore.dto.response.GroupChoreBoardResponse;
import com.cotato.cokerthon.domain.chore.dto.response.GroupChoreCalendarResponse;
import com.cotato.cokerthon.domain.chore.dto.response.GroupChoreDailyResponse;
import com.cotato.cokerthon.domain.chore.dto.response.GroupChoreResponse;
import com.cotato.cokerthon.domain.chore.service.GroupChoreService;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/chores")
@RequiredArgsConstructor
@Tag(name = "Group Chore API")
public class GroupChoreController {

    private final GroupChoreService groupChoreService;

    @Operation(summary = "집안일 추가 API", description = "완전히 새로운 집안일 항목을 만들고, 이를 바탕으로 그룹에 집안일을 추가합니다.")
    @PostMapping
    public CommonResponse<GroupChoreResponse> createChore(@PathVariable Long groupId,
                                                            @Valid @RequestBody GroupChoreCreateRequest request) {
        GroupChoreResponse response = groupChoreService.createChore(groupId, request);
        return CommonResponse.success(response);
    }

    @Operation(summary = "카탈로그 기반 집안일 추가 API", description = "미리 정의된 집안일 목록에서 항목을 선택하여 새로운 집안일을 추가합니다. 제목은 수정할 수 있고, 반복 주기는 새로 지정합니다.")
    @PostMapping("/catalog/{choreId}")
    public CommonResponse<GroupChoreResponse> createChoreFromExisting(@PathVariable Long groupId,
                                                                        @PathVariable Long choreId,
                                                                        @Valid @RequestBody GroupChoreFromCatalogRequest request) {
        GroupChoreResponse response = groupChoreService.createChoreFromExisting(groupId, choreId, request);
        return CommonResponse.success(response);
    }

    @Operation(summary = "집안일 단계별 조회 API", description = "그룹의 집안일을 예정/진행중/완료 세 단계로 나누어 조회합니다.")
    @GetMapping("/board")
    public CommonResponse<GroupChoreBoardResponse> getChoreBoard(@PathVariable Long groupId) {
        GroupChoreBoardResponse response = groupChoreService.getChoreBoard(groupId);
        return CommonResponse.success(response);
    }

    @Operation(summary = "날짜별 집안일 조회 API", description = "특정 날짜에 등록된 집안일 목록과 완료 개수를 조회합니다 (오늘의 과업). date를 생략하면 오늘 날짜로 조회합니다.")
    @GetMapping("/daily")
    public CommonResponse<GroupChoreDailyResponse> getChoresByDate(
            @PathVariable Long groupId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        GroupChoreDailyResponse response = groupChoreService.getChoresByDate(groupId, date != null ? date : LocalDate.now());
        return CommonResponse.success(response);
    }

    @Operation(summary = "캘린더용 날짜 범위 집안일 조회 API", description = "startDate부터 endDate까지 날짜별 집안일 목록을 조회합니다 (캘린더 표시용).")
    @GetMapping("/calendar")
    public CommonResponse<List<GroupChoreCalendarResponse>> getChoresByDateRange(
            @PathVariable Long groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<GroupChoreCalendarResponse> response = groupChoreService.getChoresByDateRange(groupId, startDate, endDate);
        return CommonResponse.success(response);
    }

    @Operation(summary = "집안일 진행 단계 변경 API", description = "집안일의 진행 단계를 예정/진행중/완료 중 하나로 변경합니다.")
    @PatchMapping("/{choreId}/status")
    public CommonResponse<GroupChoreResponse> updateChoreStatus(@PathVariable Long groupId,
                                                                   @PathVariable Long choreId,
                                                                   @Valid @RequestBody GroupChoreStatusUpdateRequest request) {
        GroupChoreResponse response = groupChoreService.updateChoreStatus(groupId, choreId, request);
        return CommonResponse.success(response);
    }
}