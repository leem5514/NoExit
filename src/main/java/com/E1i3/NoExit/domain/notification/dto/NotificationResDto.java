package com.E1i3.NoExit.domain.notification.dto;

import com.E1i3.NoExit.domain.notification.domain.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationResDto {
	private String sender_email;
	private String receiver_email;
	private NotificationType type;
	private String message;

	public NotificationResDto(String sender_email, String receiver_email, NotificationType type, String message) {
		this.sender_email = sender_email;
		this.receiver_email = receiver_email;
		this.type = type;
		this.message = message;
	}
}

