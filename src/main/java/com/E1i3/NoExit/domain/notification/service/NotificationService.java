package com.E1i3.NoExit.domain.notification.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.comment.dto.CommentCreateReqDto;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.member.service.MemberService;
import com.E1i3.NoExit.domain.notification.controller.NotificationController;
<<<<<<< Updated upstream
import com.E1i3.NoExit.domain.notification.dto.UserInfo;
=======
import com.E1i3.NoExit.domain.notification.domain.Notification;
import com.E1i3.NoExit.domain.notification.domain.NotificationType;
import com.E1i3.NoExit.domain.notification.dto.UserInfo;
import com.E1i3.NoExit.domain.notification.repository.NotificationRepository;
>>>>>>> Stashed changes
import com.E1i3.NoExit.domain.owner.service.OwnerService;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {
	private final Map<UserInfo, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

	private final MemberService memberService;
	private final OwnerService ownerService;
	private final NotificationRepository notificationRepository;

	@Autowired
	public NotificationService(MemberService memberService, OwnerService ownerService,
		NotificationRepository notificationRepository, MemberRepository memberRepository) {
		this.memberService = memberService;
		this.ownerService = ownerService;
		this.notificationRepository = notificationRepository;
	}

	public SseEmitter subscribe(UserInfo userInfo) {
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
<<<<<<< Updated upstream
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		// String email = (role == Role.USER) ? memberService.getEmailFromToken() : ownerService.getEmailFromToken();
		UserInfo sseEmitterKey = UserInfo.builder().email(email).role(role).build();
=======
>>>>>>> Stashed changes

		try {
			sseEmitter.send(SseEmitter.event().name("connect").data("연결 성공"));
		} catch (IOException e) {
			log.info(e.getMessage());
		}

		sseEmitters.put(userInfo, sseEmitter);
		sseEmitter.onCompletion(() -> sseEmitters.remove(userInfo));
		sseEmitter.onTimeout(() -> sseEmitters.remove(userInfo));
		sseEmitter.onError((e) -> sseEmitters.remove(userInfo));
		return sseEmitter;
	}

	private void notifyUser(UserInfo userInfo, NotificationType type, String message) {
		SseEmitter sseEmitter = sseEmitters.get(userInfo);

		String receiver = userInfo.getEmail();
		// if(userInfo.getRole() == Role.USER) {
		// 	receiver = memberService.getEmailFromToken();
		// } else if (userInfo.getRole() == Role.OWNER) {
		// 	receiver = ownerService.getEmailFromToken();
		// }
		saveNotification(receiver, type, message);

		if (sseEmitter != null) {
			try {
				sseEmitter.send(SseEmitter.event().name(type.name()).data(message));
				log.info(type.name() + " 이벤트 전송: " + message);
			} catch (IOException e) {
				log.info("SSE 전송 오류: " + e.getMessage());
				sseEmitters.remove(userInfo);
			}
		} else {
			log.info("사용자에게 전송할 SseEmitter가 없습니다: " + userInfo.getEmail());
		}
	}

	private void saveNotification(String receiver, NotificationType type, String message) {
		notificationRepository.save(
			Notification.builder()
				.email(receiver)
				.type(type)
				.message(message)
				.delyn(DelYN.N)
				.build()
		);
	}

	public String getMemberEmail() {
		return memberService.getEmailFromToken();
	}

	public String getOwnerEmail() {
		return ownerService.getEmailFromToken();
	}

	// 	1. 알림 요청시 사용자 -> 점주 예약내역 알림
	public void notifyResToOwner(ReservationSaveDto dto) {
		String senderEmail = memberService.getEmailFromToken();
		String receiverEmail = ownerService.getEmailFromReservation(dto);
		String message = senderEmail + "님이 예약을 요청하셨습니다.";
		notifyUser(UserInfo.builder().email(receiverEmail).role(Role.OWNER).build(), NotificationType.RESERVATION_REQ, message);
	}

	// 	2. 알림 승인/거절시 점주 -> 사용자 알림
	public void notifyResToMember(String receiverEmail, String approvalStatus) {
		String senderEmail = ownerService.getEmailFromToken();
		String message = senderEmail + "님이 예약을 " + approvalStatus + "하셨습니다.";
		notifyUser(UserInfo.builder().email(receiverEmail).role(Role.USER).build(), NotificationType.RESERVATION_RES, message);
	}

	// 	3. 내가 쓴 게시글에 댓글 (작성자, 댓글 내용)
	public void notifyComment(Board board, CommentCreateReqDto comment) {
		String senderEmail = memberService.getEmailFromToken();
		String receiverEmail = board.getMember().getEmail();
<<<<<<< Updated upstream
		UserInfo sseEmitterKey = UserInfo.builder().email(receiverEmail).role(Role.USER).build();
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
=======
		String message = senderEmail + "님이 게시글에 댓글을 남겼습니다.";
		notifyUser(UserInfo.builder().email(receiverEmail).role(Role.USER).build(), NotificationType.COMMENT, message);
>>>>>>> Stashed changes
	}

	// 	4. 내가 쓴 게시글에 추천
	public void notifyLikeBoard(Board board) {
		String senderEmail = memberService.getEmailFromToken();
		String receiverEmail = board.getMember().getEmail();
<<<<<<< Updated upstream
		UserInfo sseEmitterKey = UserInfo.builder().email(receiverEmail).role(Role.USER).build();
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
=======
		String message = senderEmail + "님이 내 게시글을 추천합니다.";
		notifyUser(UserInfo.builder().email(receiverEmail).role(Role.USER).build(), NotificationType.BOARD_LIKE, message);
>>>>>>> Stashed changes
	}

	// 	5. 내가 쓴 댓글 추천
	public void notifyLikeComment(Comment comment) {
		String senderEmail = memberService.getEmailFromToken();
		String receiverEmail = comment.getMember().getEmail();
<<<<<<< Updated upstream
		UserInfo sseEmitterKey = UserInfo.builder().email(receiverEmail).role(Role.USER).build();
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
		UserInfo sseEmitterKey = UserInfo.builder().email(email).role(Role.USER).build();
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
=======
		String message = senderEmail + "님이 내 댓글을 추천합니다.";
		notifyUser(UserInfo.builder().email(receiverEmail).role(Role.USER).build(), NotificationType.COMMENT_LIKE, message);
	}

	// 	6. findBoard 참여 인원 가득차면
	public void notifyFullCount(String receiverEmail) {
		String message = "참여 인원이 가득찼습니다.";
		notifyUser(UserInfo.builder().email(receiverEmail).role(Role.USER).build(), NotificationType.FULL_COUNT, message);
>>>>>>> Stashed changes
	}

}
