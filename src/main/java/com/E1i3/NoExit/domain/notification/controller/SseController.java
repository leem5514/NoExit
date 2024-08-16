package com.E1i3.NoExit.domain.notification.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.notification.domain.UserInfo;
import com.E1i3.NoExit.domain.notification.dto.NotificationResDto;
import com.E1i3.NoExit.domain.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SseController implements MessageListener {

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private Set<String> subscribeList = ConcurrentHashMap.newKeySet();

	@Qualifier("3")
	private final RedisTemplate<String, Object> sseRedisTemplate;

	private final NotificationService notificationService;
	private final RedisMessageListenerContainer redisMessageListenerContainer;

	public SseController(@Qualifier("4") RedisTemplate<String, Object> sseRedisTemplate,
		NotificationService notificationService,
		RedisMessageListenerContainer redisMessageListenerContainer) {
		this.sseRedisTemplate = sseRedisTemplate;
		this.notificationService = notificationService;
		this.redisMessageListenerContainer = redisMessageListenerContainer;
	}

	public void subscribeChannel(String email){
		if(!subscribeList.contains(email)) {
			MessageListenerAdapter listenerAdapter = createListerAdapter(this);
			redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(email));
			subscribeList.add(email);
		}
	}

	private MessageListenerAdapter createListerAdapter(SseController sseController){
		return new MessageListenerAdapter(sseController, "onMessage");
	}

	@GetMapping("/subscribe/{role}")
	public SseEmitter subcribe(@RequestParam Role role) {
		SseEmitter emitter = new SseEmitter(14400 * 60 * 1000L);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		emitters.put(email, emitter);

		emitter.onCompletion(() -> emitters.remove(email));
		emitter.onTimeout(() -> emitters.remove(email));

		// List<NotificationResDto> notifications = notificationService.getNotificationsByEmail();
		// try {
		// 	for (NotificationResDto notification : notifications) {
		// 		emitter.send(SseEmitter.event().name("notification").data(notification));
		// 	}
		// } catch (IOException e) {
		// 	e.printStackTrace();
		// }
		subscribeChannel(email);
		return emitter;
	}

	public void publishMessage(Object dto, String email ) {
		sseRedisTemplate.convertAndSend(email, dto);
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			System.out.println("message : " + message);

			ObjectMapper objectMapper = new ObjectMapper();
			NotificationResDto dto = objectMapper.readValue(message.getBody(), NotificationResDto.class);
			String email = new String(pattern, StandardCharsets.UTF_8);
			SseEmitter emitter = emitters.get(email);
			System.out.println("emitter : " + emitter);
			if (emitter != null) {
				emitter.send(SseEmitter.event().name(dto.getType().toString()).data(dto));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// private void notifyUser(String email, NotificationType type, String message) {
	// 	SseEmitter sseEmitter = emitters.get(email);
	//
	// 	if (sseEmitter != null) {
	// 		try {
	// 			sseEmitter.send(SseEmitter.event().name(type.name()).data(message));
	// 		} catch (IOException e) {
	// 			log.info("SSE 전송 오류: " + e.getMessage());
	// 			emitters.remove(email);
	// 		}
	// 	} else {
	// 		log.info("사용자에게 전송할 SseEmitter가 없습니다: " + email);
	// 	}
	// }

	// public String getMemberEmail() {
	// 	return memberService.getEmailFromToken();
	// }
	//
	// public String getOwnerEmail() {
	// 	return ownerService.getEmailFromToken();
	// }

	// 	1. 알림 요청시 사용자 -> 점주 예약내역 알림
	// public void notifyResToOwner(ReservationSaveDto dto) {
	// 	String senderEmail = getMemberEmail();
	// 	String receiverEmail = ownerService.getEmailFromReservation(dto);
	// 	String message = senderEmail + "님이 예약을 요청하셨습니다.";
	// 	notifyUser(receiverEmail, NotificationType.RESERVATION_REQ, message);
	// }

	// 	2. 알림 승인/거절시 점주 -> 사용자 알림
	// public void notifyResToMember(String receiverEmail, String approvalStatus) {
	// 	String senderEmail = ownerService.getEmailFromToken();
	// 	String message = senderEmail + "님이 예약을 " + approvalStatus + "하셨습니다.";
	// 	notifyUser(receiverEmail, NotificationType.RESERVATION_RES, message);
	// }

	// 	3. 내가 쓴 게시글에 댓글 (작성자, 댓글 내용)
	// public void notifyComment(Board board, CommentCreateReqDto comment) {
	// 	String senderEmail = memberService.getEmailFromToken();
	// 	String receiverEmail = board.getMember().getEmail();
	// 	String message = senderEmail + "님이 게시글에 댓글을 남겼습니다.";
	// 	notifyUser(receiverEmail, NotificationType.COMMENT, message);
	// }

	// 	4. 내가 쓴 게시글에 추천
	// public void notifyLikeBoard(Board board) {
	// 	// String senderEmail = memberService.getEmailFromToken();
	// 	String receiverEmail = board.getMember().getEmail();
	// 	String message = senderEmail + "님이 내 게시글을 추천합니다.";
	// 	notifyUser(receiverEmail, NotificationType.BOARD_LIKE, message);
	// }

	// 	5. 내가 쓴 댓글 추천
	// public void notifyLikeComment(Comment comment) {
	// 	String senderEmail = memberService.getEmailFromToken();
	// 	String receiverEmail = comment.getMember().getEmail();
	// 	String message = senderEmail + "님이 내 댓글을 추천합니다.";
	// 	notifyUser(receiverEmail, NotificationType.COMMENT_LIKE, message);
	//
	// }

	// 	6. findBoard 참여 인원 가득차면
	// public void notifyFullCount(){
	// 	notifyUser(UserInfo.builder().email("").role(Role.USER).build(), NotificationType.FULL_COUNT, "");
	// }
	//
	// public List<NotificationResDto> getNotificationsByEmail(String email) {
	// 	List<NotificationResDto> list = notificationRepository.findByEmail(email);
	// 	return list;
	// }


}
