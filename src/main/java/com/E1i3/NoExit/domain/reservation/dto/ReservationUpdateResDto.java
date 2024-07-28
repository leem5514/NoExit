package com.E1i3.NoExit.domain.reservation.dto;

import com.E1i3.NoExit.domain.findboard.game.domain.Game;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.reservation.domain.ApprovalStatus;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdateResDto {
    private String adminEmail;
    private Long gameId;
    private String resDate;
    private String resDateTime;
    private ApprovalStatus approvalStatus;


    public Reservation toApprovalList(Member member, Game game) {
        return Reservation.builder()
                .member(member)
                .game(game)
                .resDate(LocalDate.parse(this.resDate))
                .resDateTime(this.resDateTime)
                .approvalStatus(this.approvalStatus)
                .reservationStatus(this.approvalStatus == ApprovalStatus.OK ? ReservationStatus.ACCEPT : ReservationStatus.REJECT)
                .build();
    }
}
