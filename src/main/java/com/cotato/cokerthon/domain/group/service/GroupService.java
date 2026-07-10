package com.cotato.cokerthon.domain.group.service;

import com.cotato.cokerthon.domain.group.dto.request.GroupCreateRequest;
import com.cotato.cokerthon.domain.group.dto.request.GroupJoinRequest;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;

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
}
