package com.cotato.cokerthon.domain.group.entity;

import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_member")
@Getter
@NoArgsConstructor
public class GroupMember extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public GroupMember(Group group, Member member) {
        this.group = group;
        this.member = member;
    }
}