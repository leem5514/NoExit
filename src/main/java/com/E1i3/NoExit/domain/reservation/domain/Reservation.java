package com.E1i3.NoExit.domain.reservation.domain;

import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.owner.domain.Owner;
import com.E1i3.NoExit.domain.reservation.dto.ReservationDetailResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationListResDto;
import com.E1i3.NoExit.domain.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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

    private int numberOfPlayers; // 인원 수

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'")
    private LocalDate resDate; // 예약일(년-월-일) 만 출력

    private String resDateTime; // 예약시간

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner; // Owner 필드 추가

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 예약을 한 회원

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.PERSIST)
    private Review review;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus = ReservationStatus.WAITING; // 예약 승인에 대한 확정 상태

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus; // 사장님기준 예약 승인 거절 여부

    @CreationTimestamp
    private LocalDateTime createdAt; // 예약 당시 시간

    @Enumerated(EnumType.STRING)
    private DelYN delYN = DelYN.N;


    @Builder
    public Reservation(Owner owner, Member member, Game game, String resName, String phoneNumber, LocalDate resDate, String resDateTime,
                       int numberOfPlayers, ReservationStatus reservationStatus, ApprovalStatus approvalStatus,
                       LocalDateTime createdAt, DelYN delYN) {
        this.owner = owner;
        this.member = member;
        this.game = game;
        this.resName = resName;
        this.phoneNumber = phoneNumber;
        this.resDate = resDate;
        this.resDateTime = resDateTime;
        this.numberOfPlayers = numberOfPlayers;
        this.reservationStatus = reservationStatus;
        this.approvalStatus = approvalStatus;
        this.createdAt = createdAt;
        this.delYN = delYN.N;
    }

    public void updateStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
        if (approvalStatus == ApprovalStatus.OK) {
            this.reservationStatus = ReservationStatus.ACCEPT;
        } else if (approvalStatus == ApprovalStatus.NO) {
            this.reservationStatus = ReservationStatus.REJECT;
        }
    }

    public void updateDelYN() {
        this.delYN = DelYN.Y;
    }

    public ReservationDetailResDto toDetailDto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ReservationDetailResDto.builder()
                .id(this.id)
                .resName(this.resName)
                .phoneNumber(this.phoneNumber)
                .numberOfPlayers(this.numberOfPlayers)
                .resDate(this.resDate)
                .resDateTime(this.resDateTime)
                .reservationStatus(this.reservationStatus)
                .createdTime(this.createdAt.format(formatter))
                .build();
    }

//    public ReservationListResDto listFromEntity() {
//        return ReservationListResDto.builder()
//                .id(this.id)
//                .resName(this.resName)
//                .count(this.count)
//                .build();
//    }
//
//    public ReservationDetailResDto detailFromEntity() {
//        LocalDateTime createdTime = this.getCreatedTime();
//        String date1 = createdTime.getYear() + "년 " + createdTime.getMonthValue() + "월 " +
//                createdTime.getDayOfMonth() + "일";
//
//        return ReservationDetailResDto.builder()
//                .id(this.id)
//                .resName(this.resName)
//                .phoneNumber(this.phoneNumber)
//                .count(this.count)
//                .resDate(this.resDate)
//                .resDateTime(this.resDateTime)
//                .createdTime(date1)
//                .build();
//    }
}
