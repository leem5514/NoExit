package com.E1i3.NoExit.domain.chat.domain;

import com.E1i3.NoExit.domain.attendance.domain.Attendance;
import com.E1i3.NoExit.domain.chat.dto.ChatMessage;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chat_room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String name;
    private String password;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ChatMessageEntity> messages = new ArrayList<>();

    // @ManyToMany
    // @JoinTable(
    //         name = "chat_room_members",
    //         joinColumns = @JoinColumn(name = "room_id"),
    //         inverseJoinColumns = @JoinColumn(name = "member_id")
    // )
    //
    // //
    // @JsonIgnore // 순환 참조 방지
    // private List<Member> members = new ArrayList<>();

    // @OneToMany(mappedBy = "chatRoom")
    // private List<Attendance> attendances;


    public ChatRoom(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
