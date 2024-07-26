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

    @PostMapping("/reservation/create") // 생성
    public ResponseEntity<?> reservationCreate(@RequestBody ReservationSaveDto dto) {
        Reservation reservation = reservationService.save(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "success created", reservation.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @GetMapping("/reservation/{email}") // 이메일로 조회
    public ResponseEntity<?> reservationByEmailList(@PathVariable String email) {
        List<ReservationListResDto> reservationListResDtos = reservationService.find(email);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "success JOIN", reservationListResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/reservation/detail/{id}")
    public ResponseEntity<?> ReservationDetail(@PathVariable Long id) {
        ReservationDetailResDto commonResDto = reservationService.getReservationDetail(id);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

//    @PatchMapping("/reservation/{uuid}")
//    public ResponseEntity<?> ReservationApprovalStatusUpdate(@PathVariable UUID uuid,
//                                                             @RequestParam String adminEmail,
//                                                             @RequestParam ApprovalStatus status) {
//        Reservation reservation = reservationService.updateApprovalStatus(uuid, adminEmail, status);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Approval status updated", reservation.getId());
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }

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

    //    @GetMapping("/reservation/list")
//    public String reservationList(Model model) {
//        return "reservation/list";
//    }
//
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
}




