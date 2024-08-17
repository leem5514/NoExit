package com.E1i3.NoExit.domain.chat.service;


import com.E1i3.NoExit.domain.chat.domain.ChatMessageEntity;
import com.E1i3.NoExit.domain.chat.domain.ChatRoom;
import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
import com.E1i3.NoExit.domain.chat.repository.ChatMessageRepository;
import com.E1i3.NoExit.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Qualifier("chatRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplateChat;
    @Qualifier("chatRedisMessageListenerContainer")
    private final RedisMessageListenerContainer redisContainer;

    private final RedisMessagePublisher redisMessagePublisher;
    private final RedisStreamService redisStreamService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatMessageEntity> getMessagesForRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        return chatMessageRepository.findByChatRoom(chatRoom);
    }

    // 메시지를 처리하고 저장하는 기능
    public void handleMessage(String roomId, String sender, String content) {
        System.out.println("Handling message for room ID: " + roomId);

        // ChatRoom을 조회하여 엔티티 가져오기
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));

        System.out.println("Found chat room: " + chatRoom.getName());

        // ChatMessageEntity 빌드
        ChatMessageEntity chatMessageEntity = ChatMessageEntity.builder()
                .sender(sender)
                .content(content)
                .chatRoom(chatRoom)  // roomId 대신 chatRoom 설정
                .timestamp(System.currentTimeMillis())
                .build();

        // 메시지 저장
        chatMessageRepository.save(chatMessageEntity);
        System.out.println("Saved message to database: " + content);

        // Redis로 메시지 퍼블리시
        redisMessagePublisher.publish(content);
        System.out.println("Published message to Redis: " + content);
    }

    // 특정 방의 모든 메시지 조회
    public List<ChatMessageEntity> getMessagesByRoomId(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        return chatMessageRepository.findByChatRoom(chatRoom);
    }
}


