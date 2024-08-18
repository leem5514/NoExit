package com.E1i3.NoExit.domain.chat.service;


import com.E1i3.NoExit.domain.chat.domain.ChatRoom;
import com.E1i3.NoExit.domain.chat.repository.ChatRoomRepository;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatRoom createRoom(String name, String password) {
        ChatRoom chatRoom = new ChatRoom(name, password);
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findRoomById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }
}