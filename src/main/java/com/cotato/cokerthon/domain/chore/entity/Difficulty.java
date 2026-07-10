package com.cotato.cokerthon.domain.chore.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Difficulty {

    STAR_1(1, 5),
    STAR_2(2, 10),
    STAR_3(3, 15),
    STAR_4(4, 20),
    STAR_5(5, 25);

    @JsonValue
    private final int level;

    private final int defaultScore;
}