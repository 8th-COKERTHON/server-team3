package com.cotato.cokerthon.domain.roulette.entity;

import com.cotato.cokerthon.domain.chore.entity.Chore;
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
    @JoinColumn(name = "chore_id", nullable = false)
    private Chore chore;

    // 이 집안일을 수행해야 하는 다음 주 시작일
    @Column(nullable = false)
    private LocalDate nextWeekStartDate;

    @Builder
    public RouletteResult(Member member, Chore chore, LocalDate nextWeekStartDate) {
        this.member = member;
        this.chore = chore;
        this.nextWeekStartDate = nextWeekStartDate;
    }
}
