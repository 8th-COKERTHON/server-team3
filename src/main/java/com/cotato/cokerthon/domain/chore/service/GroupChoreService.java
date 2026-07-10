package com.cotato.cokerthon.domain.chore.service;

import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreCreateRequest;
import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreFromCatalogRequest;
import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreStatusUpdateRequest;
import com.cotato.cokerthon.domain.chore.dto.response.*;
import com.cotato.cokerthon.domain.chore.entity.*;
import com.cotato.cokerthon.domain.chore.exception.ChoreErrorCode;
import com.cotato.cokerthon.domain.chore.repository.ChoreRepository;
import com.cotato.cokerthon.domain.chore.repository.GroupChoreRepository;
import com.cotato.cokerthon.domain.group.entity.Group;
import com.cotato.cokerthon.domain.group.repository.GroupMemberRepository;
import com.cotato.cokerthon.domain.group.repository.GroupRepository;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.domain.member.repository.MemberRepository;
import com.cotato.cokerthon.domain.roulette.service.RouletteService;
import com.cotato.cokerthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupChoreService {

    // 매주/격주 반복 시 선택 가능한 요일 약어
    private static final Set<String> VALID_DAYS_OF_WEEK = Set.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

    private final GroupChoreRepository groupChoreRepository;
    private final ChoreRepository choreRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final RouletteService rouletteService;

    /**
     * 집안일 추가
     */
    @Transactional
    public GroupChoreResponse createChore(Long groupId, GroupChoreCreateRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.GROUP_NOT_FOUND));

        Member assignee = resolveAssignee(group, request.assignType(), request.assigneeId());
        validateRepeatPattern(request.repeatCycle(), request.repeatPattern());

        GroupChore groupChore = GroupChore.builder()
                .group(group)
                .assignee(assignee)
                .name(request.name())
                .date(request.date())
                .assignType(request.assignType())
                .repeatCycle(request.repeatCycle())
                .repeatPattern(request.repeatPattern())
                .memo(request.memo())
                .difficulty(request.difficulty())
                .build();

        GroupChore savedChore = groupChoreRepository.save(groupChore);

        if (request.assignType() == AssignType.ROULETTE) {
            Member winner = rouletteService.spinForChore(savedChore);
            savedChore.assignTo(winner);
        }

        return GroupChoreResponse.from(savedChore);
    }

    /**
     * 미리 정의된 집안일 목록(카탈로그)에서 항목을 선택해 새로운 집안일 추가
     */
    @Transactional
    public GroupChoreResponse createChoreFromExisting(Long groupId, Long choreId, GroupChoreFromCatalogRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.GROUP_NOT_FOUND));

        // 1. 선택한 카탈로그 항목 조회 및 그룹 소속 검증
        Chore chore = choreRepository.findById(choreId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.CHORE_ITEM_NOT_FOUND));

        if (!chore.getGroup().getId().equals(groupId)) {
            throw new CustomException(ChoreErrorCode.CHORE_ITEM_NOT_IN_GROUP);
        }

        // 2. 새로 입력받은 담당자 정보 및 반복 패턴 검증 (반복 주기는 매번 새로 지정)
        Member assignee = resolveAssignee(group, request.assignType(), request.assigneeId());
        validateRepeatPattern(request.repeatCycle(), request.repeatPattern());

        // 3. 제목은 입력값이 있으면 그 값을, 없으면 카탈로그의 제목을 사용
        String name = (request.name() != null && !request.name().isBlank()) ? request.name() : chore.getName();

        GroupChore groupChore = GroupChore.builder()
                .group(group)
                .assignee(assignee)
                .chore(chore)
                .name(name)
                .date(request.date())
                .assignType(request.assignType())
                .repeatCycle(request.repeatCycle())
                .repeatPattern(request.repeatPattern())
                .memo(request.memo())
                .build();

        GroupChore savedChore = groupChoreRepository.save(groupChore);

        if (request.assignType() == AssignType.ROULETTE) {
            Member winner = rouletteService.spinForChore(savedChore);
            savedChore.assignTo(winner);
        }

        return GroupChoreResponse.from(savedChore);
    }

    /**
     * 그룹의 집안일을 예정/진행중/완료 세 단계로 나누어 조회
     */
    public GroupChoreBoardResponse getChoreBoard(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new CustomException(ChoreErrorCode.GROUP_NOT_FOUND);
        }

        List<GroupChore> chores = groupChoreRepository.findByGroup_IdOrderByDateAsc(groupId);

        return new GroupChoreBoardResponse(
                filterByStatus(chores, ChoreStatus.SCHEDULED),
                filterByStatus(chores, ChoreStatus.IN_PROGRESS),
                filterByStatus(chores, ChoreStatus.DONE)
        );
    }

    private List<GroupChoreResponse> filterByStatus(List<GroupChore> chores, ChoreStatus status) {
        return chores.stream()
                .filter(chore -> chore.getStatus() == status)
                .map(GroupChoreResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 날짜의 집안일 조회 (오늘의 과업)
     */
    public GroupChoreDailyResponse getChoresByDate(Long groupId, LocalDate date) {
        if (!groupRepository.existsById(groupId)) {
            throw new CustomException(ChoreErrorCode.GROUP_NOT_FOUND);
        }

        List<GroupChore> chores = groupChoreRepository.findByGroup_IdAndDate(groupId, date);
        int completedCount = (int) chores.stream().filter(chore -> chore.getStatus() == ChoreStatus.DONE).count();

        List<GroupChoreResponse> responses = chores.stream()
                .map(GroupChoreResponse::from)
                .collect(Collectors.toList());

        return new GroupChoreDailyResponse(date, chores.size(), completedCount, responses);
    }

    /**
     * 날짜 범위별 집안일 조회 (캘린더 표시용)
     */
    public List<GroupChoreCalendarResponse> getChoresByDateRange(Long groupId, LocalDate startDate, LocalDate endDate) {
        if (!groupRepository.existsById(groupId)) {
            throw new CustomException(ChoreErrorCode.GROUP_NOT_FOUND);
        }

        List<GroupChore> chores = groupChoreRepository.findByGroup_IdAndDateBetween(groupId, startDate, endDate);
        Map<LocalDate, List<GroupChoreResponse>> choresByDate = chores.stream()
                .collect(Collectors.groupingBy(GroupChore::getDate,
                        Collectors.mapping(GroupChoreResponse::from, Collectors.toList())));

        List<GroupChoreCalendarResponse> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            result.add(new GroupChoreCalendarResponse(date, choresByDate.getOrDefault(date, List.of())));
        }
        return result;
    }

    /**
     * 오늘 날짜에 특정 담당자에게 배정된 집안일 조회
     */
    public GroupChoreByAssigneeResponse getTodayChoresByAssignee(Long groupId, Long memberId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.GROUP_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.ASSIGNEE_NOT_FOUND));

        if (!groupMemberRepository.existsByGroupAndMember(group, member)) {
            throw new CustomException(ChoreErrorCode.ASSIGNEE_NOT_IN_GROUP);
        }

        LocalDate today = LocalDate.now();
        List<GroupChore> memberChores = groupChoreRepository.findByGroup_IdAndDate(groupId, today).stream()
                .filter(chore -> chore.getAssignee() != null && chore.getAssignee().getId().equals(memberId))
                .toList();

        int completedCount = (int) memberChores.stream().filter(chore -> chore.getStatus() == ChoreStatus.DONE).count();
        List<GroupChoreResponse> responses = memberChores.stream()
                .map(GroupChoreResponse::from)
                .collect(Collectors.toList());

        return new GroupChoreByAssigneeResponse(today, member.getId(), member.getName(), memberChores.size(), completedCount, responses);
    }

    /**
     * 집안일 진행 단계 변경 (예정 -> 진행중 -> 완료)
     */
    @Transactional
    public GroupChoreResponse updateChoreStatus(Long groupId, Long choreId, GroupChoreStatusUpdateRequest request) {
        GroupChore chore = groupChoreRepository.findById(choreId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.CHORE_NOT_FOUND));

        if (!chore.getGroup().getId().equals(groupId)) {
            throw new CustomException(ChoreErrorCode.CHORE_NOT_IN_GROUP);
        }

        ChoreStatus previousStatus = chore.getStatus();
        ChoreStatus newStatus = request.status();

        // 기존에 완료 상태였다면, 그때 반영했던 점수를 먼저 되돌린다
        // (완료 -> 완료 취소는 물론, 완료 -> 완료(수행자 재지정)인 경우에도 재계산을 위해 반드시 필요)
        if (previousStatus == ChoreStatus.DONE) {
            revertContributionOnCancel(chore);
            chore.markPerformedBy(null);
        }

        // 새로 완료 상태가 된다면, 수행자를 지정해 점수를 다시 반영한다
        if (newStatus == ChoreStatus.DONE) {
            // 수행자를 지정하지 않으면 담당자가 직접 수행한 것으로 처리
            Member performer = request.performerId() != null
                    ? resolvePerformer(chore.getGroup(), request.performerId())
                    : chore.getAssignee();
            chore.markPerformedBy(performer);
            applyContributionOnComplete(chore, performer);
        }

        chore.updateStatus(newStatus);
        return GroupChoreResponse.from(chore);
    }

    // 실제로 집안일을 수행한 사람이 그룹에 속해있는지 확인
    private Member resolvePerformer(Group group, Long performerId) {
        Member performer = memberRepository.findById(performerId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.ASSIGNEE_NOT_FOUND));

        if (!groupMemberRepository.existsByGroupAndMember(group, performer)) {
            throw new CustomException(ChoreErrorCode.ASSIGNEE_NOT_IN_GROUP);
        }

        return performer;
    }

    // 완료 처리: 수행자에게 집안일 점수를 더하고, 담당자와 수행자가 다르면 담당자에게 감점을 적용
    private void applyContributionOnComplete(GroupChore chore, Member performer) {
        if (chore.isDelegated()) {
            chore.getAssignee().addPoint(-GroupChore.DELEGATE_PENALTY);
        }
        if (performer != null) {
            performer.addPoint(chore.getEffectiveScore());
        }
    }

    // 완료 취소: 완료 처리 때 반영했던 점수를 반대로 되돌림
    private void revertContributionOnCancel(GroupChore chore) {
        Member performer = chore.getPerformedBy();

        if (chore.isDelegated()) {
            chore.getAssignee().addPoint(GroupChore.DELEGATE_PENALTY);
        }
        if (performer != null) {
            performer.addPoint(-chore.getEffectiveScore());
        }
    }

    // 담당자 지정 방식(선택안함/직접선택/룰렛)에 따라 담당자를 결정
    private Member resolveAssignee(Group group, AssignType assignType, Long assigneeId) {
        if (assignType != AssignType.MANUAL) {
            // 선택안함, 룰렛은 생성 시점에는 담당자를 배정하지 않음
            return null;
        }

        if (assigneeId == null) {
            throw new CustomException(ChoreErrorCode.ASSIGNEE_REQUIRED);
        }

        Member assignee = memberRepository.findById(assigneeId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.ASSIGNEE_NOT_FOUND));

        if (!groupMemberRepository.existsByGroupAndMember(group, assignee)) {
            throw new CustomException(ChoreErrorCode.ASSIGNEE_NOT_IN_GROUP);
        }

        return assignee;
    }

    // 반복 주기에 따라 세부 패턴(매주/격주: 요일 선택, 매월/매년: 캘린더 날짜 선택)을 검증
    private void validateRepeatPattern(RepeatCycle repeatCycle, String repeatPattern) {
        switch (repeatCycle) {
            case WEEKLY, BIWEEKLY -> validateDayOfWeekPattern(repeatPattern);
            case MONTHLY, YEARLY -> validateCalendarDatePattern(repeatPattern);
            case NONE, DAILY -> { } // 세부 패턴 선택 불필요
        }
    }

    // 매주/격주: "MON,WED,FRI"와 같은 요일 목록만 허용
    private void validateDayOfWeekPattern(String repeatPattern) {
        for (String day : requireRepeatPattern(repeatPattern).split(",")) {
            if (!VALID_DAYS_OF_WEEK.contains(day.trim().toUpperCase())) {
                throw new CustomException(ChoreErrorCode.INVALID_REPEAT_PATTERN);
            }
        }
    }

    // 매월/매년: 캘린더에서 선택한 "yyyy-MM-dd" 형식의 날짜 목록만 허용 (예: "2026-07-15")
    private void validateCalendarDatePattern(String repeatPattern) {
        for (String date : requireRepeatPattern(repeatPattern).split(",")) {
            try {
                LocalDate.parse(date.trim());
            } catch (DateTimeParseException e) {
                throw new CustomException(ChoreErrorCode.INVALID_REPEAT_PATTERN);
            }
        }
    }

    private String requireRepeatPattern(String repeatPattern) {
        if (repeatPattern == null || repeatPattern.isBlank()) {
            throw new CustomException(ChoreErrorCode.REPEAT_PATTERN_REQUIRED);
        }
        return repeatPattern;
    }
}
