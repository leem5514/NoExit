package com.E1i3.NoExit.domain.chat.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisChatRoomManager {

	private final RedisTemplate<String, Object> redisTemplate;
	private final Map<String, Boolean> roomSubscriptions = new HashMap<>();

	public RedisChatRoomManager(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	// 방 구독 상태를 확인하고 구독 유지
	public void ensureRoomSubscription(String roomId) {
		if (!roomSubscriptions.getOrDefault(roomId, false)) {
			subscribeToRoom(roomId);
			roomSubscriptions.put(roomId, true);
		}
	}

	// 방 구독 처리
	private void subscribeToRoom(String roomId) {
		redisTemplate.convertAndSend("/topic/room/" + roomId, "Room subscription ensured");
		System.out.println("Subscribed to Redis room: " + roomId);
	}

	// 방 구독 해제 처리
	public void unsubscribeFromRoom(String roomId) {
		roomSubscriptions.remove(roomId);
		System.out.println("Unsubscribed from Redis room: " + roomId);
	}
}
