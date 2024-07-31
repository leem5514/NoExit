package com.E1i3.NoExit.domain.review.repository;

import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import com.E1i3.NoExit.domain.review.domain.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMemberAndDelYN(Member member, DelYN delYn);
    Optional<Review> findByReservationAndDelYN(Reservation reservation, DelYN delYn);


}
