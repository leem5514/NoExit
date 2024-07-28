package com.E1i3.NoExit.domain.reservation.dto;

import com.E1i3.NoExit.domain.reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailResDto {
    private Long id;

    private String resName;
    private String phoneNumber;
    private int numberOfPlayers;
    private LocalDate resDate; // 예약일
    private String resDateTime; // 예약 시간대

    private ReservationStatus reservationStatus;
//    private String store_name; // 가게 명(store 상속)
    private String createdTime;
}