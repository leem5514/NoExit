package com.E1i3.NoExit.domain.review.service;

import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import com.E1i3.NoExit.domain.review.domain.Review;
import com.E1i3.NoExit.domain.review.dto.ReviewSaveDto;
import com.E1i3.NoExit.domain.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    public ReviewService(ReviewRepository reviewRepository, MemberRepository memberRepository, ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Review createReview(ReviewSaveDto dto) {
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("없는 회원입니다"));
        Reservation reservation = reservationRepository.findById(dto.getReservationId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않은 예약입니다"));

        if (reviewRepository.findByReservationAndDelYN(reservation, DelYN.Y).isPresent()) {
            throw new IllegalStateException("이미 작성된 리뷰가 있습니다");
        }
        Review review = dto.toEntity(member, reservation);

        return reviewRepository.save(review);
    }
}
