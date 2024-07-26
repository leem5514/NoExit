package com.E1i3.NoExit.domain.reservation.controller;

import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.dto.ReservationDetailResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationListResDto;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;
import com.E1i3.NoExit.domain.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {

    @Autowired
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation/create")
    public String reservationCreateForm() {
        return "reservation/create";
    }

    @PostMapping("/reservation/create") // 생성
    public ResponseEntity<Reservation> reservationCreate(@RequestBody ReservationSaveDto reservationSaveDto) {
        Reservation savedReservation = reservationService.save(reservationSaveDto);
        return ResponseEntity.ok(savedReservation);
    }

    //    @GetMapping("/reservation/list")
//    public String reservationList(Model model) {
//        return "reservation/list";
//    }
    @GetMapping("/reservation/list")
    public ResponseEntity<List<ReservationListResDto>> getReservationsByMemberEmail(@RequestParam String email) {
        List<ReservationListResDto> reservations = reservationService.getReservationsByMemberEmail(email);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/reservation/detail/{id}")
    public ResponseEntity<ReservationDetailResDto> getReservationDetail(@PathVariable Long id) {
        ReservationDetailResDto reservationDetail = reservationService.getReservationDetail(id);
        return ResponseEntity.ok(reservationDetail);
    }

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    } // 필요 여부에 따라 삭제
}




