package com.cotato.cokerthon.domain.chore.entity;

import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chore_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChoreRequest extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    // Chore 엔티티 완성 후 @ManyToOne으로 교체
    @Column(nullable = false)
    private Long choreId;

    @Column(nullable = false)
    private String choreName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ChoreStatus status = ChoreStatus.PENDING;

    /** 받는 사람이 요청을 읽었는지 여부 */
    @Column(nullable = false)
    @Builder.Default
    private boolean isRead = false;

    /** 보낸 사람이 완료 알림을 읽었는지 여부 */
    @Column(nullable = false)
    @Builder.Default
    private boolean completionRead = false;

    public void markAsRead() {
        this.isRead = true;
    }

    public void complete() {
        this.status = ChoreStatus.COMPLETED;
    }

    public void markCompletionRead() {
        this.completionRead = true;
    }
}
