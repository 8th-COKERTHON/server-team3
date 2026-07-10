package com.cotato.cokerthon.domain.chore.controller;

import com.cotato.cokerthon.domain.chore.dto.request.ChoreRequestCreateRequest;
import com.cotato.cokerthon.domain.chore.dto.response.ChoreRequestResponse;
import com.cotato.cokerthon.domain.chore.dto.response.UnreadCountResponse;
import com.cotato.cokerthon.domain.chore.service.ChoreRequestService;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import com.cotato.cokerthon.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chore-requests")
@Tag(name = "Chore Request API")
public class ChoreRequestController {

    private final ChoreRequestService choreRequestService;

    @Operation(summary = "집안일 요청 보내기", description = "특정 멤버에게 집안일 요청을 보냅니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "수신자를 찾을 수 없음")
    })
    @PostMapping
    public CommonResponse<Void> sendRequest(
            @CurrentUser Member sender,
            @Valid @RequestBody ChoreRequestCreateRequest request
    ) {
        choreRequestService.sendRequest(sender, request);
        return CommonResponse.success(null);
    }

    @Operation(summary = "읽지 않은 알림 수 조회", description = "벨 빨간 점 표시 여부 확인용. 프론트에서 주기적으로 호출합니다.")
    @GetMapping("/unread-count")
    public CommonResponse<UnreadCountResponse> getUnreadCount(@CurrentUser Member member) {
        return CommonResponse.success(choreRequestService.getUnreadCount(member));
    }

    @Operation(summary = "알림 목록 조회", description = "벨 클릭 시 호출. 받은 요청 + 완료 알림을 최신순으로 반환하며 읽음 처리됩니다.")
    @GetMapping("/notifications")
    public CommonResponse<List<ChoreRequestResponse>> getNotifications(@CurrentUser Member member) {
        return CommonResponse.success(choreRequestService.getNotifications(member));
    }

    @Operation(summary = "요청 완료 처리", description = "받은 요청을 완료 처리합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "완료 처리 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "완료 권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "요청을 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 완료된 요청")
    })
    @PatchMapping("/{requestId}/complete")
    public CommonResponse<Void> completeRequest(
            @CurrentUser Member receiver,
            @PathVariable Long requestId
    ) {
        choreRequestService.completeRequest(receiver, requestId);
        return CommonResponse.success(null);
    }
}
