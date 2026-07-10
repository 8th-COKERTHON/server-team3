package com.cotato.cokerthon.domain.chore.entity;

import com.cotato.cokerthon.domain.group.entity.Group;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "group_chore")
@Getter
@NoArgsConstructor
public class GroupChore extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private Member assignee;

    @Column(nullable = false)
    private String name;

    // 집안일 수행 날짜 (반복인 경우 시작 날짜)
    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "assign_type", nullable = false)
    private AssignType assignType;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_cycle", nullable = false)
    private RepeatCycle repeatCycle;

    // 매주/격주: 요일 목록(예: "MON,WED,FRI"), 매월/매년: 캘린더로 선택한 날짜 목록(예: "2026-07-15")
    @Column(name = "repeat_pattern")
    private String repeatPattern;

    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false)
    private int score;

    // 진행 단계 (예정/진행중/완료)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChoreStatus status;

    @Builder
    public GroupChore(Group group, Member assignee, String name, LocalDate date,
                       AssignType assignType, RepeatCycle repeatCycle, String repeatPattern,
                       String memo, Difficulty difficulty) {
        this.group = group;
        this.assignee = assignee;
        this.name = name;
        this.date = date;
        this.assignType = assignType;
        this.repeatCycle = repeatCycle;
        this.repeatPattern = repeatPattern;
        this.memo = memo;
        this.difficulty = difficulty;
        this.score = difficulty.getDefaultScore(); // 5점부터 +5점씩 계산된 점수 반영
        this.status = ChoreStatus.SCHEDULED; // 생성 시 항상 예정 상태로 시작
    }

    // 난이도 수정 메서드
    public void updateDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.score = difficulty.getDefaultScore();
    }

    // 담당자 배정/변경 메서드 (직접선택, 룰렛 결과 반영)
    public void assignTo(Member assignee) {
        this.assignee = assignee;
    }

    // 진행 단계 변경 메서드 (예정 -> 진행중 -> 완료)
    public void updateStatus(ChoreStatus status) {
        this.status = status;
    }
}
