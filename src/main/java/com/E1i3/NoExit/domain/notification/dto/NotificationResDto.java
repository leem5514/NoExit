package com.E1i3.NoExit.domain.notification.dto;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.E1i3.NoExit.domain.common.domain.BaseTimeEntity;
import com.E1i3.NoExit.domain.notification.domain.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class NotificationResDto extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String sender_email;	// 전송
	private String email;	// 알림 받는이
	@Enumerated(EnumType.STRING)
	private NotificationType type;
	private Long board_id;
	private Long comment_id;
	private Long reservation_id;
	private String message;

	// public NotificationResDto(Long id, String sender_email, String receiver_email, NotificationType type, String message) {
	// 	this.id = id;
	// 	this.sender_email = sender_email;
	// 	this.email = receiver_email;
	// 	this.type = type;
	// 	this.message = message;
	// }
}

