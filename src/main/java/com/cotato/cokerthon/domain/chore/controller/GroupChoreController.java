package com.cotato.cokerthon.domain.chore.controller;

import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreCreateRequest;
import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreStatusUpdateRequest;
import com.cotato.cokerthon.domain.chore.dto.response.GroupChoreBoardResponse;
import com.cotato.cokerthon.domain.chore.dto.response.GroupChoreResponse;
import com.cotato.cokerthon.domain.chore.service.GroupChoreService;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups/{groupId}/chores")
@RequiredArgsConstructor
@Tag(name = "Group Chore API")
public class GroupChoreController {

    private final GroupChoreService groupChoreService;

    @Operation(summary = "집안일 추가 API", description = "그룹에 새로운 집안일을 추가합니다.")
    @PostMapping
    public CommonResponse<GroupChoreResponse> createChore(@PathVariable Long groupId,
                                                            @Valid @RequestBody GroupChoreCreateRequest request) {
        GroupChoreResponse response = groupChoreService.createChore(groupId, request);
        return CommonResponse.success(response);
    }

    @Operation(summary = "기존 집안일 기반 추가 API", description = "기존 집안일을 참고하여 새로운 집안일을 추가합니다.")
    @PostMapping("/{choreId}/copy")
    public CommonResponse<GroupChoreResponse> createChoreFromExisting(@PathVariable Long groupId,
                                                                        @PathVariable Long choreId,
                                                                        @Valid @RequestBody GroupChoreCreateRequest request) {
        GroupChoreResponse response = groupChoreService.createChoreFromExisting(groupId, choreId, request);
        return CommonResponse.success(response);
    }

    @Operation(summary = "집안일 단계별 조회 API", description = "그룹의 집안일을 예정/진행중/완료 세 단계로 나누어 조회합니다.")
    @GetMapping("/board")
    public CommonResponse<GroupChoreBoardResponse> getChoreBoard(@PathVariable Long groupId) {
        GroupChoreBoardResponse response = groupChoreService.getChoreBoard(groupId);
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