package com.cotato.cokerthon.domain.chore.entity;

import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: 다른 팀원이 구현 예정 — 현재는 컴파일용 스텁
@Entity
@Getter
@NoArgsConstructor
public class Chore extends BaseTimeEntity {

    // TODO: 다른 팀원이 필드 추가 예정

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignType assignType;

    // ROULETTE 선택 시 룰렛으로 선정된 담당자, MANUAL 선택 시 직접 지정된 담당자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_member_id")
    private Member assignedMember;

    public void assignMember(Member member) {
        this.assignedMember = member;
    }
}
