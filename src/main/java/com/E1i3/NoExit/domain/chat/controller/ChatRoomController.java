package com.E1i3.NoExit.domain.chat.controller;

import com.E1i3.NoExit.domain.chat.domain.ChatRoom;
import com.E1i3.NoExit.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat/create")
    public ChatRoom createRoom(@RequestParam String name, @RequestParam String password) {
        return chatRoomService.createRoom(name, password);
    }

    @GetMapping("/chat/rooms")
    public List<ChatRoom> roomList() {
        return chatRoomService.findAllRooms();
    }

    @GetMapping("/chat/rooms/{roomId}")
    public ChatRoom getRoom(@PathVariable Long roomId) {
        return chatRoomService.findRoomById(roomId);
    }
}
