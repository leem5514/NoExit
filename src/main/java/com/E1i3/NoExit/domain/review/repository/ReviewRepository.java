package com.E1i3.NoExit.domain.review.repository;

import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import com.E1i3.NoExit.domain.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMemberAndDelYN(Member member, DelYN delYn);
    Optional<Review> findByReservationAndDelYN(Reservation reservation, DelYN delYn);

    Page<Review> findByDelYN(DelYN delYN, Pageable pageable);

    Page<Review> findByMemberAndDelYN(Member member, DelYN delYN, Pageable pageable);
    Page<Review> findByReservation_GameIdAndDelYN(Long gameId, DelYN delYN, Pageable pageable);

}
