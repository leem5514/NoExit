package com.E1i3.NoExit.domain.chat.controller;


import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
import com.E1i3.NoExit.domain.chat.service.RedisMessagePublisher;
import com.E1i3.NoExit.domain.chat.service.RedisStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisMessagePublisher redisMessagePublisher;
    private final RedisStreamService redisStreamService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        // 메시지를 스트림에 추가
        redisStreamService.addMessageToStream(chatMessage.getRoomId(), chatMessage.getContent());

        // Redis로 메시지 퍼블리시
        redisMessagePublisher.publish(chatMessage.getContent());

        // 메시지를 특정 방으로 전송
        String destination = "/topic/room/" + chatMessage.getRoomId();
        messagingTemplate.convertAndSend(destination, chatMessage);
    }

    @MessageMapping("/chat.join")
    public void joinRoom(@Payload ChatMessage chatMessage) {
        chatMessage.setContent(chatMessage.getSender() + " joined room " + chatMessage.getRoomId() + "!");

        // 메시지를 특정 방으로 전송
        String destination = "/topic/room/" + chatMessage.getRoomId();
        messagingTemplate.convertAndSend(destination, chatMessage);
    }
}