package com.E1i3.NoExit.domain.notification.controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Role;
<<<<<<< Updated upstream
=======
import com.E1i3.NoExit.domain.member.service.MemberService;
import com.E1i3.NoExit.domain.notification.domain.Notification;
import com.E1i3.NoExit.domain.notification.domain.NotificationType;
>>>>>>> Stashed changes
import com.E1i3.NoExit.domain.notification.dto.UserInfo;
import com.E1i3.NoExit.domain.notification.service.NotificationService;
import com.E1i3.NoExit.domain.owner.service.OwnerService;
import com.E1i3.NoExit.domain.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class NotificationController {

	private final NotificationService notificationService;
<<<<<<< Updated upstream
	public static Map<UserInfo, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
=======
>>>>>>> Stashed changes

	@Autowired
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Operation(summary= "[알림] 알림 서비스")
	@GetMapping("/notification/subscribe")
<<<<<<< Updated upstream
	public SseEmitter subscribe(@RequestParam  Role role) {
		return notificationService.subscribe(role);
=======
	public SseEmitter subscribe(@RequestParam Role role) {
		String email = (role == Role.USER) ? notificationService.getMemberEmail() : notificationService.getOwnerEmail();
		UserInfo userInfo = UserInfo.builder().email(email).role(role).build();
		return notificationService.subscribe(userInfo);
>>>>>>> Stashed changes
	}
}
