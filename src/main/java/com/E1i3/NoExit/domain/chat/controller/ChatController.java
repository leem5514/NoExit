package com.E1i3.NoExit.domain.chat.controller;


import com.E1i3.NoExit.domain.chat.domain.ChatMessageEntity;
import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
import com.E1i3.NoExit.domain.chat.service.ChatService;
import com.E1i3.NoExit.domain.chat.service.RedisMessagePublisher;
//import com.E1i3.NoExit.domain.chat.service.RedisStreamService;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisMessagePublisher redisMessagePublisher;
    //    private final RedisStreamService redisStreamService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final MemberRepository memberRepository;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        try {
            String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            if (senderEmail == null) {
                throw new IllegalStateException("Authentication information not found in SecurityContextHolder");
            }
            System.out.println("/chat.sendMessage의 Sender Email: " + senderEmail);

            if (chatMessage.getSender() == null) {
                chatMessage.setSender("익명1");
            }
            Member member = memberRepository.findByEmail(senderEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid sender email: " + senderEmail));
            System.out.println("Member found: " + member.getNickname());

            // Sender를 member의 nickname으로 설정
            chatMessage.setSender(member.getNickname());

            // 메시지를 처리하고 데이터베이스에 저장
            chatService.handleMessage(chatMessage.getRoomId(), chatMessage.getSender(), chatMessage.getContent());
            // WebSocket을 통해 클라이언트로 메시지를 전송
            final String destination = "/topic/room/" + chatMessage.getRoomId();
            messagingTemplate.convertAndSend(destination, chatMessage);

            System.out.println("Message to send: " + chatMessage.getContent());

        } catch (Exception e) {
            System.err.println("Error in sendMessage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @MessageMapping("/chat.join")
    public void joinRoom(@Payload final ChatMessage chatMessage) {
        try {
            String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            if (senderEmail == null) {
                throw new IllegalStateException("Authentication information not found in SecurityContextHolder");
            }
            System.out.println("/chat.join의 Sender Email: " + senderEmail);

            if (chatMessage.getSender() == null) {
                chatMessage.setSender("익명1");
            }

            chatMessage.setContent(chatMessage.getSender() + " 님이 방에 참가하셨습니다 " + chatMessage.getRoomId() + "!");

            Member member = memberRepository.findByEmail(senderEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid sender email: " + senderEmail));
            System.out.println("Member found: " + member.getNickname());

            chatMessage.setSender(member.getNickname());
            System.out.println("Sender nickname set to: " + chatMessage.getRoomId());

            final String destination = "/topic/room/" + chatMessage.getRoomId();//
            messagingTemplate.convertAndSend(destination, chatMessage);//

            System.out.println("User joined room: " + chatMessage.getRoomId());
            System.out.println("Join message sent to WebSocket topic: " + destination);
        } catch (Exception e) {
            System.err.println("Error in joinRoom: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* 이전 채팅 내역 보기 */
    @GetMapping("/chat/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageEntity>> getMessages(@PathVariable String roomId) {
        List<ChatMessageEntity> messages = chatService.getMessagesForRoom(Long.parseLong(roomId));
        return ResponseEntity.ok(messages);
    }
}
