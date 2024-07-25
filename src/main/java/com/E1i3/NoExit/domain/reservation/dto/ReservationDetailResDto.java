package com.E1i3.NoExit.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailResDto {
    private Long id;
    private String resName;
    private String phoneNumber;
    private int count;
    private LocalDate resDate; // 예약일
    private String resDateTime; // 예약 시간대

    //    private String state; //예약 승인 . 거절 . 대기중 상태를 알려주는 컬럼
//    private String store_name; // 가게 명(store 상속)
    private String createdTime;
}