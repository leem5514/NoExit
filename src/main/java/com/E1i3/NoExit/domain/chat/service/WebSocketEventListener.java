package com.E1i3.NoExit.domain.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Service
public class WebSocketEventListener {

	private final RedisChatRoomManager redisChatRoomManager;

	public WebSocketEventListener(RedisChatRoomManager redisChatRoomManager) {
		this.redisChatRoomManager = redisChatRoomManager;
	}


	// WebSocket 연결이 이루어졌을 때 실행되는 메서드
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");  // 방 ID 가져오기
		if (roomId != null) {
			System.out.println("WebSocket 연결됨: roomId = " + roomId);
			redisChatRoomManager.ensureRoomSubscription(roomId); // Redis 구독 유지
		}
	}

	// WebSocket 연결이 끊어졌을 때 실행되는 메서드
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
		if (roomId != null) {
			System.out.println("WebSocket 연결 끊김: roomId = " + roomId);
			redisChatRoomManager.unsubscribeFromRoom(roomId); // Redis 구독 해제
		}
	}
}