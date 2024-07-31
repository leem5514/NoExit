package com.E1i3.NoExit.domain.notification.service;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.E1i3.NoExit.domain.board.repository.BoardRepository;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.comment.dto.CommentCreateReqDto;
import com.E1i3.NoExit.domain.comment.repository.CommentRepository;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.member.service.MemberService;
import com.E1i3.NoExit.domain.notification.controller.NotificationController;
import com.E1i3.NoExit.domain.notification.dto.SseEmitters;
import com.E1i3.NoExit.domain.owner.service.OwnerService;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	private final MemberService memberService;
	private final OwnerService ownerService;

	@Autowired
	public NotificationService(MemberService memberService, OwnerService ownerService) {
		this.memberService = memberService;
		this.ownerService = ownerService;
	}

	public SseEmitter subscribe(Role role) {
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		String email = (role == Role.USER) ? memberService.getEmailFromToken() : ownerService.getEmailFromToken();
		SseEmitters sseEmitterKey = SseEmitters.builder().email(email).role(role).build();

		try {
			sseEmitter.send(SseEmitter.event().name("connect"));
		} catch (IOException e) {
			log.info(e.getMessage());
		}

		NotificationController.sseEmitters.put(sseEmitterKey, sseEmitter);
		sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(sseEmitterKey));
		sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(sseEmitterKey));
		sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(sseEmitterKey));
		return sseEmitter;
	}

	// 	1. 알림 요청시 사용자 -> 점주 예약내역 알림 (ReservationSaveDto)

	// 	2. 알림 승인/거절시 점주 -> 사용자 알림 (ReservationStatus, ApprovalStatus) / (ACCEPT, Y) (REJECT, Y)

	// 	3. 내가 쓴 게시글에 댓글 (작성자, 댓글 내용)
	public void reserveToOwner(CommentCreateReqDto dto) {
		//  dto구성이 같아서 재사용
		String email = memberService.getEmailFromToken();
		SseEmitters sseEmitterKey = SseEmitters.builder().email(email).role(Role.USER).build();
		SseEmitter sseEmitter = NotificationController.sseEmitters.get(sseEmitterKey);

		log.info(sseEmitterKey.toString());
		log.info(NotificationController.sseEmitters.get(sseEmitterKey).toString());

		try {
			sseEmitter.send(sseEmitter.event()
				.name("make reservation from member to owner")
				.data(dto));
			log.info("코멘트 작성 이벤트 보내는 중");
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		log.info("코멘트 작성 이벤트 보내기 완료");

		return;
	}
	// 	4. 내가 쓴 게시글에 추천
	// 	5. 내가 쓴 게시글에 비추천

	// 	6. 내가 쓴 댓글 추천
	// 	7. 내가 쓴 댓글 비추천

}
