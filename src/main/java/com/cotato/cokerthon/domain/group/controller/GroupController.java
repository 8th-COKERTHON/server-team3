package com.cotato.cokerthon.domain.group.controller;

import com.cotato.cokerthon.domain.group.dto.request.GroupCreateRequest;
import com.cotato.cokerthon.domain.group.dto.request.GroupJoinRequest;
import com.cotato.cokerthon.domain.group.dto.response.GroupCreateResponse;
import com.cotato.cokerthon.domain.group.dto.response.GroupJoinResponse;
import com.cotato.cokerthon.domain.group.service.GroupService;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import com.cotato.cokerthon.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
