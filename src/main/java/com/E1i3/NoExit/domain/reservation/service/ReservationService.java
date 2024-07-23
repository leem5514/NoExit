package com.E1i3.NoExit.domain.reservation.service;

import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.dto.ReservationSaveDto;
import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;


    @Transactional
    public Reservation save(ReservationSaveDto dto) {
        Reservation reservation = new Reservation();
        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation;
    }




}
