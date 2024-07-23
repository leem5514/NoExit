package com.E1i3.NoExit.domain.reservation.service;

import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;


}
