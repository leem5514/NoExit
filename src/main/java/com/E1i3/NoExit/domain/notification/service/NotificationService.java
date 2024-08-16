package com.E1i3.NoExit.domain.notification.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;

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

import com.E1i3.NoExit.domain.notification.domain.Notification;
import com.E1i3.NoExit.domain.notification.domain.NotificationType;
import com.E1i3.NoExit.domain.notification.dto.UserInfo;
import com.E1i3.NoExit.domain.notification.repository.NotificationRepository;
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
	private final DataSource dataSource;

	@Autowired
	public NotificationService(MemberService memberService, OwnerService ownerService,
		NotificationRepository notificationRepository, MemberRepository memberRepository, DataSource dataSource) {
		this.memberService = memberService;
		this.ownerService = ownerService;
		this.notificationRepository = notificationRepository;
		this.dataSource = dataSource;
	}

	public SseEmitter subscribe(UserInfo userInfo) {
		SseEmitter sseEmitter = new SseEmitter(14400 * 60 * 1000L);
		// String email = (role == Role.USER) ? memberService.getEmailFromToken() : ownerService.getEmailFromToken();
		try {
			sseEmitter.send(SseEmitter.event().name("connect").data("연결 성공"));
		} catch (IOException e) {
			log.info(e.getMessage());
		}

		sseEmitters.put(userInfo, sseEmitter);
		System.out.println(sseEmitters);
		sseEmitter.onCompletion(() -> {
			log.debug("onCompletion");
			sseEmitters.remove(userInfo);
		});
		sseEmitter.onTimeout(() -> {
			log.debug("onTimeout");
			sseEmitters.remove(userInfo);
		});
		sseEmitter.onError((e) -> {
			log.debug("onError");
			sseEmitters.remove(userInfo);
		});
		System.out.println(sseEmitters);
		return sseEmitter;
	}

	private void notifyUser(UserInfo userInfo, NotificationType type, String message) {
		SseEmitter sseEmitter = sseEmitters.get(userInfo);

		String receiver = userInfo.getEmail();
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
		try (Connection connection = dataSource.getConnection()) {
			// 알림 저장을 위한 데이터베이스 작업 수행
			notificationRepository.save(
				Notification.builder()
					.email(receiver)
					.type(type)
					.message(message)
					.build()
			);
		} catch (SQLException e) {
			log.error("데이터베이스 연결 오류: ", e);
		}
	}

	public String getMemberEmail() {
		return memberService.getEmailFromToken();
	}

	public String getOwnerEmail() {
		return ownerService.getEmailFromToken();
	}

	// 	1. 알림 요청시 사용자 -> 점주 예약내역 알림
	public void notifyResToOwner(ReservationSaveDto dto) {
		String senderEmail = getMemberEmail();
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
		String message = senderEmail + "님이 게시글에 댓글을 남겼습니다.";
		notifyUser(UserInfo.builder().email(receiverEmail).role(Role.USER).build(), NotificationType.COMMENT, message);
	}

	// 	4. 내가 쓴 게시글에 추천
	public void notifyLikeBoard(Board board) {
		String senderEmail = memberService.getEmailFromToken();
		String receiverEmail = board.getMember().getEmail();
		String message = senderEmail + "님이 내 게시글을 추천합니다.";
		notifyUser(UserInfo.builder().email(receiverEmail).role(Role.USER).build(), NotificationType.BOARD_LIKE, message);
	}

	// 	5. 내가 쓴 댓글 추천
	public void notifyLikeComment(Comment comment) {
		String senderEmail = memberService.getEmailFromToken();
		String receiverEmail = comment.getMember().getEmail();
		String message = senderEmail + "님이 내 댓글을 추천합니다.";
		notifyUser(UserInfo.builder().email(receiverEmail).role(Role.USER).build(), NotificationType.COMMENT_LIKE, message);

	}

	// 	6. findBoard 참여 인원 가득차면
	public void notifyFullCount(){
		notifyUser(UserInfo.builder().email("").role(Role.USER).build(), NotificationType.FULL_COUNT, "");
	}

}
