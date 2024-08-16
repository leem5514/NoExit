//package com.E1i3.NoExit.domain.chat.controller;
//
//
//import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
//import com.E1i3.NoExit.domain.chat.service.RedisMessagePublisher;
//import com.E1i3.NoExit.domain.chat.service.RedisStreamService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class ChatController {
//
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(ChatMessage chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(ChatMessage chatMessage) {
//        chatMessage.setContent(chatMessage.getSender() + " joined!");
//        return chatMessage;
//    }
//}