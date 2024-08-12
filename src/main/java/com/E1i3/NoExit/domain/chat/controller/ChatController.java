package com.E1i3.NoExit.domain.chat.controller;

import com.E1i3.NoExit.domain.chat.domain.ChatRoom;
import com.E1i3.NoExit.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chat")
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @DeleteMapping("/chat/delete")
    public void deleteRoom(@RequestParam String name) {
        chatService.deleteRoom(name);
    }

//    @GetMapping
//    public Result findAllRoom() {
//        List<ChatDto> allRoom = chatService.findAllRoom();
//        return new ChatRoomResponse(allRoom, allRoom.size());
//    }


}
