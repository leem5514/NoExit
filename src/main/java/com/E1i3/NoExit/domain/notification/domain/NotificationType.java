package com.E1i3.NoExit.domain.notification.domain;

public enum NotificationType {
	COMMENT,	// 댓글
	BOARD_LIKE,	// 게시글 추천
	COMMENT_LIKE,	// 댓글 추천
	RESERVATION_REQ,	// 예약 요청
	RESERVATION_RES,	// 예약 승인, 거절
	FULL_COUNT,	// 참여인원 가득참
	CHAT_ROOM_INVITE
}
