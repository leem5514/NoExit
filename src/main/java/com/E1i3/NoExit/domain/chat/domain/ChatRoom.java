package com.E1i3.NoExit.domain.chat.domain;

import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
import com.E1i3.NoExit.domain.chat.service.ChatService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handlerActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        // message 에 담긴 타입을 확인한다.
        // 이때 message 에서 getType 으로 가져온 내용이
        // ChatDTO 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            // sessions 에 넘어온 session 을 담고,
            sessions.add(session);
            // message 에는 입장하였다는 메시지를 띄운다
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
            sendMessage(chatMessage, chatService);
        } else if (chatMessage.getType().equals(ChatMessage.MessageType.TALK)){
            //메세지 보내기
            chatMessage.setMessage(chatMessage.getMessage());
            sendMessage(chatMessage, chatService);
        }
    }


    private <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }
}