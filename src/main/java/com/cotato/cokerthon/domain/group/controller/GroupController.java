package com.cotato.cokerthon.domain.group.controller;

import com.cotato.cokerthon.domain.group.dto.request.GroupCreateRequest;
import com.cotato.cokerthon.domain.group.dto.request.GroupJoinRequest;
import com.cotato.cokerthon.domain.group.dto.response.GroupChoreReportResponse;
import com.cotato.cokerthon.domain.group.dto.response.GroupCreateResponse;
import com.cotato.cokerthon.domain.group.dto.response.GroupInviteCodeResponse;
import com.cotato.cokerthon.domain.group.dto.response.GroupJoinResponse;
import com.cotato.cokerthon.domain.group.service.GroupService;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import com.cotato.cokerthon.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "Group API")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 생성 API", description = "그룹을 생성하고 초대 코드를 반환합니다.")
    @PostMapping
    public CommonResponse<GroupCreateResponse> createGroup(@CurrentUser Member member,
                                                             @Valid @RequestBody GroupCreateRequest request) {
        GroupCreateResponse response = groupService.createGroup(member.getId(), request);
        return CommonResponse.success(response);
    }

    @Operation(summary = "그룹 가입 API", description = "초대 코드로 그룹에 가입합니다.")
    @PostMapping("/join")
    public CommonResponse<GroupJoinResponse> joinGroup(@CurrentUser Member member,
                                                         @Valid @RequestBody GroupJoinRequest request) {
        GroupJoinResponse response = groupService.joinGroup(member.getId(), request);
        return CommonResponse.success(response);
    }

    @Operation(summary = "그룹 초대 코드 조회 API", description = "내가 가입한 그룹의 초대 코드를 조회합니다.")
    @GetMapping("/{groupId}/invite-code")
    public CommonResponse<GroupInviteCodeResponse> getGroupInviteCode(@CurrentUser Member member,
                                                                      @PathVariable("groupId") Long groupId) {
        GroupInviteCodeResponse response = groupService.getGroupInviteCode(member.getId(), groupId);
        return CommonResponse.success(response);
    }

    @Operation(summary = "주간 집안일 기여도 리포트 조회 API", description = "그룹원별로 특정 주차에 완료한 집안일 점수를 합산하여 기여도 랭킹을 조회합니다. targetWeek는 조회하고 싶은 주차에 속한 임의의 날짜(yyyy-MM-dd)입니다.")
    @GetMapping("/{groupId}/chore-report")
    public CommonResponse<GroupChoreReportResponse> getGroupChoreReport(@CurrentUser Member member,
                                                                          @PathVariable("groupId") Long groupId,
                                                                          @RequestParam String targetWeek) {
        GroupChoreReportResponse response = groupService.getGroupChoreReport(member.getId(), groupId, targetWeek);
        return CommonResponse.success(response);
    }
}