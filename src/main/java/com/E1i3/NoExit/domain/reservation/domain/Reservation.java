package com.E1i3.NoExit.domain.reservation.domain;

import com.E1i3.NoExit.domain.game.domain.Game;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예약 번호

    private String resName; // 예약자 명

    private String phoneNumber; // 핸드폰 번호

    private int count; // 인원 수

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'")
    private LocalDate resDate; // 예약일(년-월-일) 만 출력

    private String resDateTime; // 예약시간

    // (더미데이터로 받는) GAME DB에서 게임명 , 예약시간, 최대 인원수 정보 받아오기!
    @OneToOne
    @JoinColumn(name = "author_id")
    private Game game;

    @Enumerated
    private State state;

    @CreationTimestamp
    private LocalDateTime createdTime; // 예약 당시 시간
    //    private boolean notificationState; // 알림 상태

    @Builder
    public Reservation(String resName, String phoneNumber, int count, LocalDate resDate, String resDateTime, Game game, State state) {
        this.resName = resName;
        this.phoneNumber = phoneNumber;
        this.count = count;
        this.resDate = resDate;
        this.resDateTime = resDateTime;
        this.game = game;//
        this.state = state;
    }



}
