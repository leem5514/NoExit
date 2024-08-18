package com.E1i3.NoExit.domain.chat.controller;


import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
import com.E1i3.NoExit.domain.chat.service.ChatService;
import com.E1i3.NoExit.domain.chat.service.RedisMessagePublisher;
//import com.E1i3.NoExit.domain.chat.service.RedisStreamService;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

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
        String senderEmail = null;
        try {
            // Check if the authentication is present
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new IllegalStateException("No authentication found in SecurityContextHolder");
            }

            // Retrieve the sender's email
            senderEmail = authentication.getName();
            if (senderEmail == null) {
                throw new IllegalStateException("No sender email found in authentication");
            }
            System.out.println("/chat.sendMessage의 Sender email: " + senderEmail);

            // Find the member by email
            String finalSenderEmail = senderEmail;
            Member member = memberRepository.findByEmail(senderEmail)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + finalSenderEmail));

            // Set the sender's nickname
            chatMessage.setSender(member.getNickname());

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

            chatMessage.setContent(chatMessage.getSender() + " joined room " + chatMessage.getRoomId() + "!");

            System.out.println("Executing findByEmail with senderEmail: " + senderEmail);
            Member member = memberRepository.findByEmail(senderEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid sender email: " + senderEmail));
            System.out.println("Member found: " + member.getNickname());


            chatMessage.setSender(member.getNickname());
            System.out.println("Sender nickname set to: " + chatMessage.getRoomId());

            final String destination = "/topic/room/" + chatMessage.getRoomId();
            messagingTemplate.convertAndSend(destination, chatMessage);

            System.out.println("User joined room: " + chatMessage.getRoomId());
            System.out.println("Join message sent to WebSocket topic: " + destination);
        } catch (Exception e) {
            System.err.println("Error in joinRoom: " + e.getMessage());
            e.printStackTrace();
        }
    }


}