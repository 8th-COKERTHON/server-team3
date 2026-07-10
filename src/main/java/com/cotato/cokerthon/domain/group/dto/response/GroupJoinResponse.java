package com.cotato.cokerthon.domain.group.dto.response;

import com.cotato.cokerthon.domain.group.entity.Group;

public record GroupJoinResponse(
        Long groupId,
        String groupName
) {
    public static GroupJoinResponse from(Group group) {
        return new GroupJoinResponse(
                group.getId(),
                group.getName()
        );
    }
}
