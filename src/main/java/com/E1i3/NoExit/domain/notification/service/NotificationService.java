package com.E1i3.NoExit.domain.notification.service;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.E1i3.NoExit.domain.board.repository.BoardRepository;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.comment.repository.CommentRepository;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.member.service.MemberService;
import com.E1i3.NoExit.domain.notification.controller.NotificationController;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	private final MemberService memberService;

	@Autowired
	public NotificationService(MemberService memberService) {
		this.memberService = memberService;
	}

	public SseEmitter subscribe() {
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		String email = memberService.getEmailFromToken();

		log.info("email : " + email);
		try {
			sseEmitter.send(SseEmitter.event().name("connect"));
		} catch (IOException e) {
			log.info(e.getMessage());
		}

		NotificationController.sseEmitters.put(email, sseEmitter);
		sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(email));
		sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(email));
		sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(email));
		return sseEmitter;
	}
}
