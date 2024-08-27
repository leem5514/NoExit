package com.E1i3.NoExit.domain.chat.service;


import com.E1i3.NoExit.domain.attendance.domain.Attendance;
import com.E1i3.NoExit.domain.attendance.repositroy.AttendanceRepository;
import com.E1i3.NoExit.domain.chat.domain.ChatRoom;
import com.E1i3.NoExit.domain.chat.repository.ChatRoomRepository;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository;

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

    public List<ChatRoom> getChatRoomsForMember(String email) {
        List<Attendance> attendances = attendanceRepository.findByEmailAndChatRoomIsNotNull(email);
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (Attendance attendance : attendances) {
            chatRooms.add(attendance.getChatRoom());
        }
        return chatRooms;
    }
}