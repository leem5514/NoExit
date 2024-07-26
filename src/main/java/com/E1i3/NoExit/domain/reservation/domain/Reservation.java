package com.E1i3.NoExit.domain.reservation.domain;

import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.reservation.dto.ReservationDetailResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationListResDto;
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

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 예약을 한 회원

    @Enumerated
    private State state;

    @CreationTimestamp
    private LocalDateTime createdTime; // 예약 당시 시간

    @Builder
    public Reservation(String resName, String phoneNumber, int count, LocalDate resDate, String resDateTime, Game game, Member member, State state) {
        this.resName = resName;
        this.phoneNumber = phoneNumber;
        this.count = count;
        this.resDate = resDate;
        this.resDateTime = resDateTime;
        this.game = game;
        this.member = member;
        this.state = state;
    }

    public ReservationListResDto listFromEntity() {
        return ReservationListResDto.builder()
                .id(this.id)
                .resName(this.resName)
                .count(this.count)
                .build();
    }

    public ReservationDetailResDto detailFromEntity() {
        LocalDateTime createdTime = this.getCreatedTime();
        String date1 = createdTime.getYear() + "년 " + createdTime.getMonthValue() + "월 " +
                createdTime.getDayOfMonth() + "일";

        return ReservationDetailResDto.builder()
                .id(this.id)
                .resName(this.resName)
                .phoneNumber(this.phoneNumber)
                .count(this.count)
                .resDate(this.resDate)
                .resDateTime(this.resDateTime)
                .createdTime(date1)
                .build();
    }
}
