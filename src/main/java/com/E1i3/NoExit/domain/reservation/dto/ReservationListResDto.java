package com.E1i3.NoExit.domain.reservation.dto;

import com.E1i3.NoExit.domain.reservation.domain.DelYN;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationListResDto {
    private String resName;
    private String phoneNumber;
    private int numberOfPlayers;
    private LocalDate resDate;
    private String resDateTime;
    private ReservationStatus reservationStatus;


    public static ReservationListResDto listFromEntity(Reservation reservation) {
        return ReservationListResDto.builder()
                .resName(reservation.getResName())
                .phoneNumber(reservation.getPhoneNumber())
                .numberOfPlayers(reservation.getNumberOfPlayers())
                .resDate(reservation.getResDate())
                .resDateTime(reservation.getResDateTime())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}