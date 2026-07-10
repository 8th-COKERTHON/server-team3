package com.cotato.cokerthon.domain.chore.entity;

import com.cotato.cokerthon.domain.group.entity.Group;
import com.cotato.cokerthon.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chore")
@Getter
@NoArgsConstructor
public class Chore extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    private String category;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false)
    private int score;

    private String memo;

    @Builder
    public Chore(Group group, String category, String name, Difficulty difficulty, String memo) {
        this.group = group;
        this.category = category;
        this.name = name;
        this.difficulty = difficulty;
        this.score = difficulty.getDefaultScore(); // 난이도별 기본 점수 자동 매핑
        this.memo = memo;
    }

    public void updateDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.score = difficulty.getDefaultScore();
    }
}