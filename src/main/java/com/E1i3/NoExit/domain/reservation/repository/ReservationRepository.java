package com.E1i3.NoExit.domain.reservation.repository;

import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMemberEmail(String email);
    Optional<Reservation> findByReservationUuid(UUID reservationUuid);
}

