package com.E1i3.NoExit.domain.reservation.dto;


import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSaveDto {
    private String resName;
    private String phoneNumber;
    private int count;
    private LocalDate resDate;
    private String resDateTime;

    public Reservation toEntity() {
        Reservation reservation = Reservation.builder()
                .resName(this.resName)
                .phoneNumber(this.phoneNumber)
                .count(this.count)
                .game(new Game())
                .resDate(this.resDate)
                .resDateTime(this.resDateTime)
                .build();
        return reservation;
    }
}
