package com.cotato.cokerthon.domain.chore.service;

import com.cotato.cokerthon.domain.chore.dto.request.GroupChoreCreateRequest;
import com.cotato.cokerthon.domain.chore.dto.response.GroupChoreResponse;
import com.cotato.cokerthon.domain.chore.entity.AssignType;
import com.cotato.cokerthon.domain.chore.entity.GroupChore;
import com.cotato.cokerthon.domain.chore.entity.RepeatCycle;
import com.cotato.cokerthon.domain.chore.exception.ChoreErrorCode;
import com.cotato.cokerthon.domain.chore.repository.GroupChoreRepository;
import com.cotato.cokerthon.domain.group.entity.Group;
import com.cotato.cokerthon.domain.group.repository.GroupMemberRepository;
import com.cotato.cokerthon.domain.group.repository.GroupRepository;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.domain.member.repository.MemberRepository;
import com.cotato.cokerthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupChoreService {

    // 매주/격주 반복 시 선택 가능한 요일 약어
    private static final Set<String> VALID_DAYS_OF_WEEK = Set.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

    private final GroupChoreRepository groupChoreRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;

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
        return GroupChoreResponse.from(savedChore);
    }

    /**
     * 기존 집안일을 기반으로 새로운 집안일 추가
     */
    @Transactional
    public GroupChoreResponse createChoreFromExisting(Long groupId, Long existingChoreId, GroupChoreCreateRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.GROUP_NOT_FOUND));

        // 1. 복사 대상이 되는 기존 집안일 조회
        GroupChore existingChore = groupChoreRepository.findById(existingChoreId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.CHORE_NOT_FOUND));

        // 2. 새로 입력받은 담당자 정보 및 반복 패턴 검증
        Member assignee = resolveAssignee(group, request.assignType(), request.assigneeId());
        validateRepeatPattern(request.repeatCycle(), request.repeatPattern());

        // 3. 기존 값 + 수정된 값 조합하여 새 엔티티 빌드
        GroupChore newGroupChore = GroupChore.builder()
                .group(group)
                .assignee(assignee) // 새로 지정한 담당자
                .name(request.name()) // 수정 가능하게 입력받은 새 이름
                .difficulty(request.difficulty()) // 수정 가능하게 입력받은 새 난이도
                .date(request.date()) // 새로 지정한 날짜
                .assignType(request.assignType()) // 새로 지정한 담당자 할당 타입
                .repeatCycle(request.repeatCycle()) // 새로 지정한 반복 주기
                .repeatPattern(request.repeatPattern()) // 새로 지정한 반복 패턴
                .memo(request.memo() != null ? request.memo() : existingChore.getMemo()) // request에 메모가 없으면 기존 메모 유지
                .build();

        GroupChore savedChore = groupChoreRepository.save(newGroupChore);
        return GroupChoreResponse.from(savedChore);
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
