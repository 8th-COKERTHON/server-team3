package com.cotato.cokerthon.domain.group.dto.response;

import com.cotato.cokerthon.domain.group.entity.Group;
import com.cotato.cokerthon.domain.group.entity.GroupMember;

import java.util.List;

public record GroupMembersResponse(
        String groupName,
        List<MemberInfo> members
) {
    public record MemberInfo(Long memberId, String name) {}

    public static GroupMembersResponse from(Group group, List<GroupMember> groupMembers) {
        List<MemberInfo> members = groupMembers.stream()
                .map(gm -> new MemberInfo(gm.getMember().getId(), gm.getMember().getName()))
                .toList();
        return new GroupMembersResponse(group.getName(), members);
    }
}
