package com.E1i3.NoExit.domain.chat.service;

import com.E1i3.NoExit.domain.chat.domain.ChatRoom;
import com.E1i3.NoExit.domain.chat.dto.ChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    //활성화된 모든 채팅방을 조회
//    public List<ChatDto> findAllRoom() {
//        List<ChatDto> collect = chatRooms.values().stream().map(chatRoom -> new ChatDto(chatRoom.getRoomId(), chatRoom.getName(), (long) chatRoom.getSessions().size())).collect(Collectors.toList());
//        return collect;
//    }
    //채팅방 하나를 조회
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    //새로운 방 생성
    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }
    //방 삭제
    public void deleteRoom(String roomId) {
        ChatRoom chatRoom = findRoomById(roomId);
        //해당방에 아무도 없다면 자동 삭제
        if(chatRoom.getSessions().size() == 0) chatRooms.remove(roomId);
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}