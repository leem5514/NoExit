//package com.E1i3.NoExit.domain.chat.service;
//
//
//import com.E1i3.NoExit.domain.chat.domain.ChatMessageEntity;
//import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
//import com.E1i3.NoExit.domain.chat.repository.ChatMessageRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ChatService {
//
//    private final ChatMessageRepository chatMessageRepository;
//
//    public ChatService(ChatMessageRepository chatMessageRepository) {
//        this.chatMessageRepository = chatMessageRepository;
//    }
//
//    public void saveMessage(ChatMessage chatMessage) {
//        ChatMessageEntity messageEntity = new ChatMessageEntity();
//        messageEntity.setRoomId(chatMessage.getRoomId());
//        messageEntity.setSender(chatMessage.getSender());
//        messageEntity.setContent(chatMessage.getContent());
//        messageEntity.setTimestamp(System.currentTimeMillis());
//
//        chatMessageRepository.save(messageEntity);
//    }
//
//    public List<ChatMessageEntity> getChatHistory(String roomId) {
//        return chatMessageRepository.findByRoomId(roomId);
//    }
//}