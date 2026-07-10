package com.cotato.cokerthon.domain.group.service;

import com.cotato.cokerthon.domain.chore.entity.ChoreStatus;
import com.cotato.cokerthon.domain.chore.entity.GroupChore;
import com.cotato.cokerthon.domain.chore.repository.GroupChoreRepository;
import com.cotato.cokerthon.domain.group.dto.request.GroupCreateRequest;
import com.cotato.cokerthon.domain.group.dto.request.GroupJoinRequest;
import com.cotato.cokerthon.domain.group.dto.response.GroupChoreReportResponse;
import com.cotato.cokerthon.domain.group.dto.response.GroupCreateResponse;
import com.cotato.cokerthon.domain.group.dto.response.GroupInviteCodeResponse;
import com.cotato.cokerthon.domain.group.dto.response.GroupJoinResponse;
import com.cotato.cokerthon.domain.group.entity.Group;
import com.cotato.cokerthon.domain.group.entity.GroupMember;
import com.cotato.cokerthon.domain.group.exception.GroupErrorCode;
import com.cotato.cokerthon.domain.group.repository.GroupMemberRepository;
import com.cotato.cokerthon.domain.group.repository.GroupRepository;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.domain.member.repository.MemberRepository;
import com.cotato.cokerthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final GroupChoreRepository groupChoreRepository;

    /**
     * 1. 그룹 생성 (생성자는 자동으로 그룹멤버에 추가)
     */
    @Transactional
    public GroupCreateResponse createGroup(Long creatorId, GroupCreateRequest request) {
        Member creator = memberRepository.findById(creatorId)
                .orElseThrow(() -> new CustomException(GroupErrorCode.MEMBER_NOT_FOUND));

        // 그룹 생성 (생성자 내부에서 inviteCode 자동 생성됨)
        Group group = new Group(request.groupName());
        groupRepository.save(group);

        // 생성자를 그룹 멤버로 등록
        GroupMember groupMember = new GroupMember(group, creator);
        groupMemberRepository.save(groupMember);

        return GroupCreateResponse.from(group);
    }

    /**
     * 2. 초대 코드로 그룹 가입
     */
    @Transactional
    public GroupJoinResponse joinGroup(Long memberId, GroupJoinRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(GroupErrorCode.MEMBER_NOT_FOUND));

        // 초대 코드로 그룹 조회
        Group group = groupRepository.findByInviteCode(request.inviteCode().toUpperCase())
                .orElseThrow(() -> new CustomException(GroupErrorCode.INVALID_INVITE_CODE));

        // 이미 가입된 회원인지 검증
        if (groupMemberRepository.existsByGroupAndMember(group, member)) {
            throw new CustomException(GroupErrorCode.ALREADY_JOINED_GROUP);
        }

        // 그룹 멤버로 추가
        GroupMember groupMember = new GroupMember(group, member);
        groupMemberRepository.save(groupMember);

        return GroupJoinResponse.from(group);
    }

    /**
     * 3. 내가 가입한 그룹의 초대 코드 조회
     */
    public GroupInviteCodeResponse getGroupInviteCode(Long memberId, Long groupId) {
        // 1. 회원 및 그룹 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(GroupErrorCode.MEMBER_NOT_FOUND));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND));

        // 2. 해당 그룹의 멤버가 맞는지 검증
        if (!groupMemberRepository.existsByGroupAndMember(group, member)) {
            throw new CustomException(GroupErrorCode.NOT_GROUP_MEMBER);
        }

        return GroupInviteCodeResponse.from(group);
    }

    /**
     * 그룹 내 특정 주차의 집안일 기여도 및 랭킹 조회
     * targetWeek: 조회하고 싶은 주차에 속한 임의의 날짜 (yyyy-MM-dd)
     */
    public GroupChoreReportResponse getGroupChoreReport(Long memberId, Long groupId, String targetWeek) {
        // 1. 요청한 유저가 해당 그룹의 멤버인지 먼저 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(GroupErrorCode.MEMBER_NOT_FOUND));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GroupErrorCode.GROUP_NOT_FOUND));

        if (!groupMemberRepository.existsByGroupAndMember(group, member)) {
            throw new CustomException(GroupErrorCode.NOT_GROUP_MEMBER);
        }

        // 2. 그룹에 속한 전체 멤버 조회
        List<GroupMember> groupMembers = groupMemberRepository.findByGroup(group);
        if (groupMembers.isEmpty()) {
            throw new CustomException(GroupErrorCode.NO_GROUP_MEMBERS);
        }

        // 3. targetWeek이 속한 주(해당 월의 n주차, 7일 단위)의 날짜 범위와 라벨 계산
        LocalDate referenceDate = LocalDate.parse(targetWeek);
        int weekOfMonth = ((referenceDate.getDayOfMonth() - 1) / 7) + 1;
        LocalDate monthStart = referenceDate.withDayOfMonth(1);
        LocalDate weekStart = monthStart.plusDays((long) (weekOfMonth - 1) * 7);
        LocalDate monthEnd = referenceDate.withDayOfMonth(referenceDate.lengthOfMonth());
        LocalDate weekEnd = weekStart.plusDays(6).isAfter(monthEnd) ? monthEnd : weekStart.plusDays(6);
        String weekLabel = "%d년 %02d월 %d주차".formatted(referenceDate.getYear(), referenceDate.getMonthValue(), weekOfMonth);

        // 4. 해당 주차에 완료된 집안일을 조회하여, 담당자/수행자가 다르면 담당자는 감점, 수행자는 점수를 얻도록 멤버별로 집계
        List<GroupChore> doneChores = groupChoreRepository.findByGroup_IdAndStatusAndDateBetween(
                groupId, ChoreStatus.DONE, weekStart, weekEnd);

        Map<Long, Integer> scoreByMemberId = new HashMap<>();
        for (GroupChore chore : doneChores) {
            Member performer = chore.getPerformedBy();

            if (chore.isDelegated()) {
                scoreByMemberId.merge(chore.getAssignee().getId(), -GroupChore.DELEGATE_PENALTY, Integer::sum);
            }
            if (performer != null) {
                scoreByMemberId.merge(performer.getId(), chore.getEffectiveScore(), Integer::sum);
            }
        }

        int totalScore = 0;
        List<MemberScoreDto> memberScores = new ArrayList<>();

        for (GroupMember gm : groupMembers) {
            Member groupUser = gm.getMember();
            int score = Math.max(0, scoreByMemberId.getOrDefault(groupUser.getId(), 0));

            totalScore += score;
            memberScores.add(new MemberScoreDto(groupUser, score));
        }

        // 5. 점수 기준 내림차순 정렬 (랭킹 산정을 위해)
        memberScores.sort((a, b) -> Integer.compare(b.score(), a.score()));

        // 6. 순위(Rank) 및 비율(%) 계산하여 Response DTO 변환
        List<GroupChoreReportResponse.MemberChoreRankInfo> rankInfos = new ArrayList<>();
        int currentRank = 1;

        for (int i = 0; i < memberScores.size(); i++) {
            MemberScoreDto current = memberScores.get(i);

            // 동점자 처리 로직 (이전 멤버와 점수가 같으면 같은 순위 유지)
            if (i > 0 && current.score() < memberScores.get(i - 1).score()) {
                currentRank = i + 1;
            }

            // 도넛 파이 비율 계산 (총점이 0점일 때의 예외 처리 포함)
            double ratio = (totalScore == 0) ? 0.0 : Math.round(((double) current.score() / totalScore) * 1000) / 10.0; // 소수점 첫째자리까지 반올림

            rankInfos.add(new GroupChoreReportResponse.MemberChoreRankInfo(
                    currentRank,
                    current.member().getId(),
                    current.member().getName(),
                    current.score(),
                    ratio
            ));
        }

        return new GroupChoreReportResponse(weekLabel, rankInfos);
    }

    // 서비스 내부에서만 사용할 임시 DTO record
    private record MemberScoreDto(Member member, int score) {}
}
