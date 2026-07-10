package com.cotato.cokerthon.domain.chore.controller;

import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreCreateRequest;
import com.cotato.cokerthon.domain.chore.dto.response.GroupChoreResponse;
import com.cotato.cokerthon.domain.chore.service.GroupChoreService;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}