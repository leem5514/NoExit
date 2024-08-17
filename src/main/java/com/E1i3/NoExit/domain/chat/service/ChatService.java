package com.E1i3.NoExit.domain.chat.service;


import com.E1i3.NoExit.domain.chat.domain.ChatMessageEntity;
import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
import com.E1i3.NoExit.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RedisMessagePublisher redisMessagePublisher;
    private final RedisStreamService redisStreamService;

    public void handleMessage(String roomId, String message) {
        // 메시지를 Redis 스트림에 저장
        redisStreamService.addMessageToStream(roomId, message);

        // 메시지를 Redis 채널에 퍼블리시
        redisMessagePublisher.publish(message);
    }
}
