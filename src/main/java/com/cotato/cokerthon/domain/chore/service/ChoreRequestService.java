package com.cotato.cokerthon.domain.chore.service;

import com.cotato.cokerthon.domain.chore.dto.request.ChoreRequestCreateRequest;
import com.cotato.cokerthon.domain.chore.dto.response.ChoreRequestResponse;
import com.cotato.cokerthon.domain.chore.dto.response.UnreadCountResponse;
import com.cotato.cokerthon.domain.chore.entity.ChoreRequest;
import com.cotato.cokerthon.domain.chore.entity.ChoreStatus;
import com.cotato.cokerthon.domain.chore.exception.ChoreErrorCode;
import com.cotato.cokerthon.domain.chore.repository.ChoreRequestRepository;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.domain.member.repository.MemberRepository;
import com.cotato.cokerthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChoreRequestService {

    private final ChoreRequestRepository choreRequestRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void sendRequest(Member sender, ChoreRequestCreateRequest request) {
        Member receiver = memberRepository.findById(request.receiverId())
                .orElseThrow(() -> new CustomException(ChoreErrorCode.CHORE_REQUEST_NOT_FOUND));

        ChoreRequest choreRequest = ChoreRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .choreId(request.choreId())
                .choreName(request.choreName())
                .build();

        choreRequestRepository.save(choreRequest);
    }

    public UnreadCountResponse getUnreadCount(Member member) {
        long receivedUnread = choreRequestRepository.countByReceiverAndIsReadFalse(member);
        long completionUnread = choreRequestRepository.countBySenderAndStatusAndCompletionReadFalse(member, ChoreStatus.DONE);
        return new UnreadCountResponse(receivedUnread + completionUnread);
    }

    @Transactional
    public List<ChoreRequestResponse> getReceivedRequests(Member receiver) {
        List<ChoreRequest> requests = choreRequestRepository.findByReceiverOrderByCreatedAtDesc(receiver);

        requests.stream()
                .filter(r -> !r.isRead())
                .forEach(ChoreRequest::markAsRead);

        return requests.stream()
                .map(ChoreRequestResponse::fromReceiverView)
                .toList();
    }

    @Transactional
    public List<ChoreRequestResponse> getSentRequests(Member sender) {
        List<ChoreRequest> requests = choreRequestRepository.findBySenderOrderByCreatedAtDesc(sender);

        requests.stream()
                .filter(r -> r.getStatus() == ChoreStatus.DONE && !r.isCompletionRead())
                .forEach(ChoreRequest::markCompletionRead);

        return requests.stream()
                .filter(r -> r.getStatus() == ChoreStatus.DONE)
                .map(ChoreRequestResponse::fromSenderView)
                .toList();
    }

    @Transactional
    public void completeRequest(Member receiver, Long requestId) {
        ChoreRequest choreRequest = choreRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ChoreErrorCode.CHORE_REQUEST_NOT_FOUND));

        if (!choreRequest.getReceiver().getId().equals(receiver.getId())) {
            throw new CustomException(ChoreErrorCode.UNAUTHORIZED_DONE);
        }

        if (choreRequest.getStatus() == ChoreStatus.DONE) {
            throw new CustomException(ChoreErrorCode.ALREADY_DONE);
        }

        choreRequest.complete();
    }
}
