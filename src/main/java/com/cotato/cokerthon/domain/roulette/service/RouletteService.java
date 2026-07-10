package com.cotato.cokerthon.domain.roulette.service;

import com.cotato.cokerthon.domain.chore.entity.GroupChore;
import com.cotato.cokerthon.domain.group.entity.Group;
import com.cotato.cokerthon.domain.group.entity.GroupMember;
import com.cotato.cokerthon.domain.group.repository.GroupMemberRepository;
import com.cotato.cokerthon.domain.group.repository.GroupRepository;
import com.cotato.cokerthon.domain.member.entity.Member;
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
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    // GroupChoreService에서 호출 — assignType == ROULETTE일 때 담당자 선정 후 결과 저장
    @Transactional
    public Member spinForChore(GroupChore groupChore) {
        List<Member> members = getGroupMembers(groupChore.getGroup());
        Member winner = pickWinner(members);
        rouletteResultRepository.save(RouletteResult.builder()
                .member(winner)
                .groupChore(groupChore)
                .nextWeekStartDate(groupChore.getDate())
                .build());
        return winner;
    }

    // 룰렛 지분 조회 (프론트 룰렛 UI 렌더링용)
    public List<RouletteSliceResponse> getSlices(Long groupId) {
        List<Member> members = getGroupMembers(findGroup(groupId));
        return computeSlices(members);
    }

    // 룰렛 결과 이력 조회
    public List<RouletteResultResponse> getResults(Long groupId, LocalDate nextWeekStartDate) {
        return rouletteResultRepository
                .findByGroupChore_GroupIdAndNextWeekStartDate(groupId, nextWeekStartDate)
                .stream()
                .map(RouletteResultResponse::from)
                .toList();
    }

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(RouletteErrorCode.GROUP_NOT_FOUND));
    }

    private List<Member> getGroupMembers(Group group) {
        List<Member> members = groupMemberRepository.findByGroup(group).stream()
                .map(GroupMember::getMember)
                .toList();
        if (members.isEmpty()) {
            throw new CustomException(RouletteErrorCode.NO_MEMBERS);
        }
        return members;
    }

    // totalPoints 역수 기반 룰렛 지분 계산
    // 1/(total_point+1) → 0점도 자연스럽게 처리, 많이 할수록 확률 감소
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
