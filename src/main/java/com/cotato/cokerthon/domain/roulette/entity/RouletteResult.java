package com.cotato.cokerthon.domain.roulette.entity;

import com.cotato.cokerthon.domain.chore.entity.GroupChore;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class RouletteResult extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_chore_id", nullable = false)
    private GroupChore groupChore;

    @Column(nullable = false)
    private LocalDate nextWeekStartDate;

    @Builder
    public RouletteResult(Member member, GroupChore groupChore, LocalDate nextWeekStartDate) {
        this.member = member;
        this.groupChore = groupChore;
        this.nextWeekStartDate = nextWeekStartDate;
    }
}
