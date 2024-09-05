package com.E1i3.NoExit.domain.chat.controller;

import com.E1i3.NoExit.domain.chat.domain.ChatMessageEntity;
import com.E1i3.NoExit.domain.chat.domain.ChatRoom;
import com.E1i3.NoExit.domain.chat.dto.CreateRoomRequest;
import com.E1i3.NoExit.domain.chat.repository.ChatMessageRepository;
import com.E1i3.NoExit.domain.chat.service.ChatRoomService;
import com.E1i3.NoExit.domain.chat.service.ChatService;
import com.E1i3.NoExit.domain.chat.service.RedisChatRoomManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisChatRoomManager redisChatRoomManager;
   
    private Map<String, List<String>> chatRooms = new HashMap<>();


    public ChatRoomController(ChatRoomService chatRoomService, ChatService chatService, ChatMessageRepository chatMessageRepository,
		RedisChatRoomManager redisChatRoomManager) {
        this.chatRoomService = chatRoomService;
        this.chatService = chatService;
        this.chatMessageRepository = chatMessageRepository;
		this.redisChatRoomManager = redisChatRoomManager;
	}

    @PostMapping("/createRoom")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody CreateRoomRequest request) {
        ChatRoom chatRoom = chatRoomService.createRoom(request.getName(), request.getPassword());
        return ResponseEntity.ok(chatRoom);
    }
    @PostMapping("/joinRoom")
    public ResponseEntity<String> joinRoom(@RequestBody Map<String, String> payload) {
        String roomId = payload.get("roomId");
        String username = payload.get("username");

        ChatRoom chatRoom = chatRoomService.findRoomById(Long.parseLong(roomId));
        if (chatRoom == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
        }

        // 사용자 추가 로직이 필요하다면 추가 구현
        return ResponseEntity.ok("Joined room");
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> roomList() {
        return ResponseEntity.ok(chatRoomService.findAllRooms());
    }

    @GetMapping("/rooms/{roomId}")
   public ResponseEntity<ChatRoom> getRoom(@PathVariable Long roomId) {
       return ResponseEntity.ok(chatRoomService.findRoomById(roomId));
   }
   // @GetMapping("/rooms/{roomId}/messages")
   // public ResponseEntity<List<ChatMessageEntity>> getMessages(@PathVariable Long roomId) {
   //     ChatRoom chatRoom = chatRoomService.findRoomById(roomId);
   //     if (chatRoom == null) {
   //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
   //     }
   //     List<ChatMessageEntity> messages = chatMessageRepository.findByChatRoom(chatRoom);
   //     return ResponseEntity.ok(messages);
   // }

    @GetMapping("/myrooms")
    public ResponseEntity<List<ChatRoom>> getMyChatRooms() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ChatRoom> chatRooms = chatRoomService.getChatRoomsForMember(email);
        return ResponseEntity.  ok(chatRooms);
    }

    @PostMapping("/ensure-subscription")
    public String ensureSubscription(@RequestBody Map<String, String> payload) {
        String roomId = payload.get("roomId");
        redisChatRoomManager.ensureRoomSubscription(roomId);
        return "Subscription ensured for room " + roomId;
    }
}


