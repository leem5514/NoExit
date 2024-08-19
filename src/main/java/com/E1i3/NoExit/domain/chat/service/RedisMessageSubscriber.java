package com.E1i3.NoExit.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


// 발행한 메세지 수신, websocket을 통하여 클라이언트 전달
@Service
public class RedisMessageSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public RedisMessageSubscriber(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        String channel = new String(message.getChannel());
        System.out.println("Received message from Redis: " + body + " on channel: " + channel);

        Object content;
        try {
            content = objectMapper.readValue(body, Object.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize content", e);
        }

        messagingTemplate.convertAndSend("/topic/room", content);
        System.out.println("Sent message to WebSocket topic: /topic/room");
    }
}