package com.E1i3.NoExit.domain.reservation.domain;

import com.E1i3.NoExit.domain.game.domain.Game;
import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예약 번호

    private String name; // 예약자 명

    private int count; // 인원 수

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'", timezone = "Asia/Seoul")
    private LocalDateTime resDate; // 예약일(년-월-일) 만 출력

    private String resDateTime; // 예약시간
//    private Long resDateTime;

    // (더미데이터로 받는) GAME DB에서 게임명 , 예약시간, 최대 인원수 정보 받아오기!
    @OneToOne
    @JoinColumn(name = "author_id")
    private Game game;

    @Enumerated
    private State state;


}
