package com.E1i3.NoExit.domain.reservation.controller;

import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;
import com.E1i3.NoExit.domain.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReservationController {
    @Autowired
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation/list")
    public String reservationList(Model model) {
        return "reservation/list";
    }

    @GetMapping("/reservation/create")
    public String reservationCreateForm() {
        return "reservation/create";
    }

    @PostMapping("/reservation/create")
    public String reservationCreate(@ModelAttribute ReservationSaveDto dto) {
        reservationService.save(dto);
        return "redirect:/";
    }

}
