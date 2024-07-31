package com.E1i3.NoExit.domain.reservation.dto;

import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.owner.domain.Owner;
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


    public Reservation toApprovalList(Owner owner, Game game) {
        return Reservation.builder()
                .owner(owner)
                .game(game)
                .resDate(LocalDate.parse(this.resDate))
                .resDateTime(this.resDateTime)
                .approvalStatus(this.approvalStatus)
                .reservationStatus(this.approvalStatus == ApprovalStatus.OK ? ReservationStatus.ACCEPT : ReservationStatus.REJECT)
                .build();
    }
}
