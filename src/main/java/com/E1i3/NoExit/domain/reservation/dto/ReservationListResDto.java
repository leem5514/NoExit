package com.E1i3.NoExit.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationListResDto {
    private Long id;
    private String resName;
    private int count;
    private String state;
    //    private String state; //예약 승인 . 거절 . 대기중 상태를 알려주는 컬럼
    private String memberEmail;
}