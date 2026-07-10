package com.cotato.cokerthon.domain.weeklyreport.entity;

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
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "weekStartDate"})
})
public class WeeklyReport extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate weekStartDate;

    private int totalPoints;

    // 일을 안 한 비율을 역산한 룰렛 지분 퍼센트 (0~100)
    // 기여도가 낮을수록 값이 커져 다음 주 집안일 당첨 확률 상승
    private double shareRatio;

    private Integer rank;

    @Builder
    public WeeklyReport(Member member, LocalDate weekStartDate, int totalPoints) {
        this.member = member;
        this.weekStartDate = weekStartDate;
        this.totalPoints = totalPoints;
    }

    public void updateShareRatioAndRank(double shareRatio, int rank) {
        this.shareRatio = shareRatio;
        this.rank = rank;
    }
}
