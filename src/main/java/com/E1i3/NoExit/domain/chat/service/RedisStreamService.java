package com.E1i3.NoExit.domain.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisStreamService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addMessageToStream(String roomId, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        redisTemplate.opsForStream().add("chat-room:" + roomId, map);
    }
}