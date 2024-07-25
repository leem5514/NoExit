package com.E1i3.NoExit.domain.reservation.dto;

import com.E1i3.NoExit.domain.reservation.domain.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdateResDto {
    private UUID reservationUuid;
    private String adminEmail;
    private ApprovalStatus approvalStatus;
}
