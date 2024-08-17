package com.E1i3.NoExit.domain.chat.controller;


import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
import com.E1i3.NoExit.domain.chat.service.ChatService;
import com.E1i3.NoExit.domain.chat.service.RedisMessagePublisher;
import com.E1i3.NoExit.domain.chat.service.RedisStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisMessagePublisher redisMessagePublisher;
    private final RedisStreamService redisStreamService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {

        System.out.println("Received message to send: " + chatMessage.getContent());            //

        // 메시지를 Redis 스트림에 추가
        redisStreamService.addMessageToStream(chatMessage.getRoomId(), chatMessage.getContent());

        System.out.println("Added message to Redis stream: " + chatMessage.getContent());      //

        // 메시지를 Redis 채널에 퍼블리시
        redisMessagePublisher.publish(chatMessage.getContent());

        System.out.println("Published message to Redis channel: " + chatMessage.getContent());       //

        chatService.handleMessage(chatMessage.getRoomId(), chatMessage.getSender(), chatMessage.getContent());

        System.out.println("Handled and saved message: " + chatMessage.getContent());             //

        // WebSocket을 통해 같은 방에 있는 클라이언트들에게 메시지 전송
        String destination = "/topic/room/" + chatMessage.getRoomId();
        messagingTemplate.convertAndSend(destination, chatMessage);

        System.out.println("Message sent to WebSocket topic: " + destination);           //
    }

    @MessageMapping("/chat.join")
    public void joinRoom(@Payload ChatMessage chatMessage) {
        chatMessage.setContent(chatMessage.getSender() + " joined the room!");

        System.out.println("User joined room: " + chatMessage.getRoomId());             //

        // WebSocket을 통해 방에 참여했다는 메시지를 브로드캐스트
        String destination = "/topic/room/" + chatMessage.getRoomId();
        messagingTemplate.convertAndSend(destination, chatMessage);
        System.out.println("Join message sent to WebSocket topic: " + destination);             //
    }
}