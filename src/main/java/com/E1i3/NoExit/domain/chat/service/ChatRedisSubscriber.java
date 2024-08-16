package com.E1i3.NoExit.domain.chat.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class ChatRedisSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Redis로부터 메시지를 수신하면 처리
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        System.out.println("Received message: " + body + " from channel: " + channel);
    }
}