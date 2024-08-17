package com.E1i3.NoExit.domain.chat.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 메시지 처리 로직 (예: 메시지를 WebSocket을 통해 브로드캐스트)
        System.out.println("Received message: " + message.toString());
    }
}

