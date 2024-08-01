package com.E1i3.NoExit.domain.notification.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.notification.dto.SseEmitters;
import com.E1i3.NoExit.domain.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class NotificationController {

	private final NotificationService notificationService;
	public static Map<SseEmitters, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

	@Autowired
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Operation(summary= "[알림] 알림 서비스")
	@GetMapping("/notification/subscribe")
	public SseEmitter subscribe(@RequestParam  Role role) {
		System.out.println("before: " + sseEmitters);
		// log.info(role.toString());
		return notificationService.subscribe(role);
	}


}
