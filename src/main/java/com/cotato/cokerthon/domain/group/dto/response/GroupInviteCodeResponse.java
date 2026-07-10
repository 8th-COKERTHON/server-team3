package com.cotato.cokerthon.domain.group.dto.response;

import com.cotato.cokerthon.domain.group.entity.Group;

public record GroupInviteCodeResponse(
        Long groupId,
        String inviteCode
) {
    public static GroupInviteCodeResponse from(Group group) {
        return new GroupInviteCodeResponse(
                group.getId(),
                group.getInviteCode()
        );
    }
}