package com.E1i3.NoExit.domain.notification.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.comment.dto.CommentCreateReqDto;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.member.service.MemberService;
import com.E1i3.NoExit.domain.notification.controller.NotificationController;
import com.E1i3.NoExit.domain.notification.dto.SseEmitters;
import com.E1i3.NoExit.domain.owner.service.OwnerService;

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
		/* 503 Service Unavailable 방지용 dummy event 전송 */

		NotificationController.sseEmitters.put(sseEmitterKey, sseEmitter);
		sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(sseEmitterKey));
		sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(sseEmitterKey));
		sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(sseEmitterKey));
		return sseEmitter;
	}

	// 	1. 알림 요청시 사용자 -> 점주 예약내역 알림 (ReservationSaveDto)
	public void notifyResToOwner(){

	}

	// 	2. 알림 승인/거절시 점주 -> 사용자 알림 (ReservationStatus, ApprovalStatus) / (ACCEPT, Y) (REJECT, Y)
	public void notifyResToMember(){

	}

	// 	3. 내가 쓴 게시글에 댓글 (작성자, 댓글 내용)
	public void notifyComment(Board board, CommentCreateReqDto comment) {
		String senderEmail = memberService.getEmailFromToken();
		String receiverEmail = board.getMember().getEmail();
		SseEmitters sseEmitterKey = SseEmitters.builder().email(receiverEmail).role(Role.USER).build();
		SseEmitter sseEmitter = NotificationController.sseEmitters.get(sseEmitterKey);

		log.info(sseEmitterKey.toString());
		log.info(NotificationController.sseEmitters.get(sseEmitterKey).toString());

		try {
			sseEmitter.send(sseEmitter.event()
				.name("댓글")    // xx님이 보내는 답글
				.data(senderEmail + "님이 보내는 답글"));
			log.info("코멘트 작성 이벤트 보내는 중");
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		log.info("코멘트 작성 이벤트 보내기 완료");
		return;
	}

	// 	4. 내가 쓴 게시글에 추천
	public void notifyLikeBoard(Board board) {
		//  board -> board.member.email
		// board 작성자에게 이벤트 발생
		String senderEmail = memberService.getEmailFromToken();
		String receiverEmail = board.getMember().getEmail();
		SseEmitters sseEmitterKey = SseEmitters.builder().email(receiverEmail).role(Role.USER).build();
		SseEmitter sseEmitter = NotificationController.sseEmitters.get(sseEmitterKey);

		log.info(sseEmitterKey.toString());
		log.info(NotificationController.sseEmitters.get(sseEmitterKey).toString());

		try {
			sseEmitter.send(sseEmitter.event()
				// xx님이 내 게시글을 추천합니다.
				.name("게시글 추천")
				.data(senderEmail + "님이 내 게시글을 추천합니다."));
			log.info("게시글 추천 이벤트 보내는 중");
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		log.info("게시글 추천 이벤트 보내기 완료");
	}

	// 	5. 내가 쓴 댓글 추천
	public void notifyLikeComment(Comment comment) {
		String senderEmail = memberService.getEmailFromToken();
		String receiverEmail = comment.getMember().getEmail();
		SseEmitters sseEmitterKey = SseEmitters.builder().email(receiverEmail).role(Role.USER).build();
		SseEmitter sseEmitter = NotificationController.sseEmitters.get(sseEmitterKey);

		log.info(sseEmitterKey.toString());
		log.info(NotificationController.sseEmitters.get(sseEmitterKey).toString());

		try {
			sseEmitter.send(sseEmitter.event()
				// xx님이 내 게시글을 추천합니다.
				.name("댓글 추천")
				.data(senderEmail + "님이 내 답글을 추천합니다."));

			log.info("코멘트 추천 이벤트 보내는 중");
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		log.info("코멘트 추천 이벤트 보내기 완료");
	}

	// 	6. findBoard 참여 인원 가득차면
	public void notifyFullCount(){
		String email = memberService.getEmailFromToken();
		SseEmitters sseEmitterKey = SseEmitters.builder().email(email).role(Role.USER).build();
		SseEmitter sseEmitter = NotificationController.sseEmitters.get(sseEmitterKey);

		log.info(sseEmitterKey.toString());
		log.info(NotificationController.sseEmitters.get(sseEmitterKey).toString());

		try {
			sseEmitter.send(sseEmitter.event()
				// xx님이 내 게시글을 추천합니다.
				.name("참여 불가")
				.data("참여인원이 가득찼습니다."));

			log.info("참여인원 제한 이벤트 보내는 중");
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		log.info("참여인원 제한 이벤트 보내기 완료");
	}

}
