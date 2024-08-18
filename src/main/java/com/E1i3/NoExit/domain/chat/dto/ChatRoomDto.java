package com.E1i3.NoExit.domain.chat.dto;

import com.E1i3.NoExit.domain.chat.domain.ChatRoom;

// 순환참조 방지용
public class ChatRoomDto {
    private Long roomId;
    private String name;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.name = chatRoom.getName();
    }
}