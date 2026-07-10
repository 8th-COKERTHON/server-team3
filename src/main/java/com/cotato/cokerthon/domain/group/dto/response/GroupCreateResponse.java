package com.cotato.cokerthon.domain.group.dto.response;

import com.cotato.cokerthon.domain.group.entity.Group;

public record GroupCreateResponse(
        Long groupId,
        String groupName,
        String inviteCode
) {
    public static GroupCreateResponse from(Group group) {
        return new GroupCreateResponse(
                group.getId(),
                group.getName(),
                group.getInviteCode()
        );
    }
}
