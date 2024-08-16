package com.E1i3.NoExit.domain.notification.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.notification.dto.NotificationResDto;
import com.E1i3.NoExit.domain.notification.service.NotificationService;
import com.E1i3.NoExit.domain.owner.service.OwnerService;
import com.E1i3.NoExit.domain.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class NotificationController {

	private final NotificationService notificationService;


	@Autowired
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	// @Operation(summary = "[알림] 알림 서비스")
	// @GetMapping("/notification/subscribe")
	// public SseEmitter subscribe() {
	// 	return notificationService.subscribe();
	// }

	@GetMapping("/notification/list")
	public ResponseEntity<?> getNotification() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		List<NotificationResDto> list = notificationService.getNotificationsByEmail();
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "알림 목록을 불러왔습니다.", list.size()), HttpStatus.OK);

	}
}
