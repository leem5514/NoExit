package com.E1i3.NoExit.domain.notification.controller;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.E1i3.NoExit.domain.common.dto.CommonErrorDto;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {

	private final NotificationService notificationService;


	@Autowired
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	// 게시글 전체 조회
	@GetMapping("/list")
	public ResponseEntity<?> getNotification() {
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "알림 목록을 불러왔습니다.",  notificationService.getNotificationsByEmail()), HttpStatus.OK);
	}

	// 게시글 읽음 상태 변경
	@GetMapping("/update/{id}")
	public ResponseEntity<?> NotificationUpdate(@PathVariable Long id){
		try {
			notificationService.updateDelYN(id);
			CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "알림을 확인하였습니다", id);
			return new ResponseEntity<>(commonResDto, HttpStatus.OK);
		}catch (EntityNotFoundException e){
			e.printStackTrace();
			CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, "알림을 확인하는데 문제가 발생하였습니다");
			return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
		}
	}
}
