package com.E1i3.NoExit.domain.chat.controller;

import com.E1i3.NoExit.domain.chat.domain.ChatRoom;
import com.E1i3.NoExit.domain.chat.dto.CreateRoomRequest;
import com.E1i3.NoExit.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")  // 기본 경로를 설정
public class ChatRoomController {

    private Map<String, List<String>> chatRooms = new HashMap<>();

    @PostMapping("/createRoom")
    public ResponseEntity<String> createRoom(@RequestBody Map<String, String> payload) {
        String roomId = UUID.randomUUID().toString();
        chatRooms.put(roomId, new ArrayList<>());
        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/joinRoom")
    public ResponseEntity<String> joinRoom(@RequestBody Map<String, String> payload) {
        String roomId = payload.get("roomId");
        String username = payload.get("username");

        if (!chatRooms.containsKey(roomId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
        }

        chatRooms.get(roomId).add(username);
        return ResponseEntity.ok("Joined room");
    }
}


