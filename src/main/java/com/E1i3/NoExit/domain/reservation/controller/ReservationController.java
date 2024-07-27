package com.E1i3.NoExit.domain.reservation.controller;

import com.E1i3.NoExit.domain.common.CommonResDto;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.reservation.domain.ApprovalStatus;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.dto.ReservationDetailResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationListResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationUpdateResDto;
import com.E1i3.NoExit.domain.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ReservationController {

    @Autowired
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

//    @GetMapping("/reservation/create")
//    public String reservationCreateForm() {
//        return "reservation/create";
//    }
    /* 예약 */
    @PostMapping("/reservation/create") // 생성
    public ResponseEntity<?> reservationCreate(@RequestBody ReservationSaveDto dto) {
        Reservation reservation = reservationService.save(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "success created", reservation.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    /* 유저별 자신의 예약 조회 */
    @GetMapping("/reservation/{email}") // 이메일로 조회
    public ResponseEntity<?> reservationByEmailList(@PathVariable String email) {
        List<ReservationListResDto> reservationListResDtos = reservationService.find(email);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "success JOIN", reservationListResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    /* 예약 확정 처리*/
    @PutMapping("/reservation/approval")
    public ResponseEntity<?> updateApprovalStatus(@RequestBody ReservationUpdateResDto dto) {
        try {
            Reservation updatedReservation = reservationService.updateApprovalStatus(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Approval status updated", updatedReservation.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
    /* 특정 일 특정 시간대 에 대한 예약 여부 조회 */
    /* 후 일수에 관한 예약 가능 시간대로 변경 */
    @GetMapping("/reservation/time")
    public ResponseEntity<?> findReservation(@RequestParam String resDate, @RequestParam String resDateTime) {
        Optional<Reservation> reservation = reservationService.findReservationTime(resDate, resDateTime);
        if (reservation.isPresent()) {
            return new ResponseEntity<>(reservation.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.NOT_FOUND, "Reservation not found", null), HttpStatus.NOT_FOUND);
        }
    }
    /* 이메일과 예약 ID를 기준으로 예약 상세 조회 */
    @GetMapping("/reservation/{email}/{id}")
    public ResponseEntity<?> findReservationDetail(@PathVariable String email, @PathVariable Long id) {
        ReservationDetailResDto reservationDetail = reservationService.ReservationDetail(email, id);
        return new ResponseEntity<>(reservationDetail, HttpStatus.OK);
    }



    //    @GetMapping("/reservation/list")
//    public String reservationList(Model model) {
//        return "reservation/list";
//    }
//
//    @GetMapping("/reservation/detail/{id}")
//    public ResponseEntity<ReservationDetailResDto> getReservationDetail(@PathVariable Long id) {
//        ReservationDetailResDto reservationDetail = reservationService.getReservationDetail(id);
//        return ResponseEntity.ok(reservationDetail);
//    }
//
//    @DeleteMapping("/reservation/{id}")
//    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
//        reservationService.delete(id);
//        return ResponseEntity.noContent().build();
//    } // 필요 여부에 따라 삭제
//    @PutMapping("/reservation/approval")
//    public ResponseEntity<?> reservationApprovalStatusUpdate(@RequestBody ReservationUpdateResDto dto) {
//        Reservation updatereservation = reservationService.updateApprovalStatus(dto);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Approval status updated", updatereservation.getId());
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }
    //    @PatchMapping("/reservation/{uuid}")
//    public ResponseEntity<?> ReservationApprovalStatusUpdate(@PathVariable UUID uuid,
//                                                             @RequestParam String adminEmail,
//                                                             @RequestParam ApprovalStatus status) {
//        Reservation reservation = reservationService.updateApprovalStatus(uuid, adminEmail, status);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Approval status updated", reservation.getId());
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }
}




