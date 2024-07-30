package com.E1i3.NoExit.domain.notification.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.E1i3.NoExit.domain.notification.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class NotificationController {

	private final NotificationService notificationService;
	public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

	@Autowired
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@GetMapping("/notification/subscribe")
	public SseEmitter subscribe() {
		System.out.println("before: " + sseEmitters);
		return notificationService.subscribe();
	}
}
