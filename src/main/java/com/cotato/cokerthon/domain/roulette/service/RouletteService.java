package com.cotato.cokerthon.domain.roulette.service;

import com.cotato.cokerthon.domain.chore.entity.Chore;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.domain.member.repository.MemberRepository;
import com.cotato.cokerthon.domain.roulette.dto.response.RouletteResultResponse;
import com.cotato.cokerthon.domain.roulette.dto.response.RouletteSliceResponse;
import com.cotato.cokerthon.domain.roulette.entity.RouletteResult;
import com.cotato.cokerthon.domain.roulette.exception.RouletteErrorCode;
import com.cotato.cokerthon.domain.roulette.repository.RouletteResultRepository;
import com.cotato.cokerthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RouletteService {

    private final RouletteResultRepository rouletteResultRepository;
    private final MemberRepository memberRepository;

    // ChoreService에서 호출 — Chore 생성/수정 시 repeatType == ROULETTE이면 이 메서드로 담당자 선정
    @Transactional
    public Member spinForChore(Chore chore, LocalDate assignedDate) {
        List<Member> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new CustomException(RouletteErrorCode.NO_MEMBERS);
        }

        Member winner = pickWinner(members);

        rouletteResultRepository.save(RouletteResult.builder()
                .member(winner)
                .chore(chore)
                .nextWeekStartDate(assignedDate)
                .build());

        return winner;
    }

    // 룰렛 지분 조회 (프론트 룰렛 UI 렌더링용)
    public List<RouletteSliceResponse> getSlices() {
        List<Member> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new CustomException(RouletteErrorCode.NO_MEMBERS);
        }
        return computeSlices(members);
    }

    // 룰렛 결과 이력 조회
    public List<RouletteResultResponse> getResults(LocalDate nextWeekStartDate) {
        return rouletteResultRepository.findByNextWeekStartDate(nextWeekStartDate).stream()
                .map(RouletteResultResponse::from)
                .toList();
    }

    // totalPoints 역수 기반 룰렛 지분 계산
    // 1/(totalPoints+1) 사용 → 0점도 자연스럽게 처리, 많이 할수록 확률 감소
    private List<RouletteSliceResponse> computeSlices(List<Member> members) {
        double[] inverses = members.stream()
                .mapToDouble(m -> 1.0 / (m.getTotal_point() + 1))
                .toArray();

        double totalInverse = 0;
        for (double inv : inverses) totalInverse += inv;

        List<RouletteSliceResponse> slices = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            double ratio = inverses[i] / totalInverse * 100.0;
            slices.add(new RouletteSliceResponse(members.get(i).getId(), ratio));
        }
        return slices;
    }

    private Member pickWinner(List<Member> members) {
        List<RouletteSliceResponse> slices = computeSlices(members);
        double random = new Random().nextDouble() * 100.0;
        double cumulative = 0.0;
        for (int i = 0; i < slices.size(); i++) {
            cumulative += slices.get(i).shareRatio();
            if (random <= cumulative) {
                return members.get(i);
            }
        }
        return members.get(members.size() - 1);
    }
}
