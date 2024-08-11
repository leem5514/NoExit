package com.E1i3.NoExit.domain.chat.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Data
public class ChatRoomResponse {
    private List<ChatDto> rooms;
    private int count;

    public ChatRoomResponse(List<ChatDto> rooms, int count) {
        this.rooms = rooms;
        this.count = count;
    }

}