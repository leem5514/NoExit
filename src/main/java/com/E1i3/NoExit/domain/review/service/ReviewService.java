package com.E1i3.NoExit.domain.review.service;

import com.E1i3.NoExit.domain.common.service.S3Uploader;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import com.E1i3.NoExit.domain.review.domain.DelYN;
import com.E1i3.NoExit.domain.review.domain.Review;
import com.E1i3.NoExit.domain.review.dto.ReviewSaveDto;
import com.E1i3.NoExit.domain.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private S3Uploader s3Uploader; //
    public ReviewService(ReviewRepository reviewRepository, MemberRepository memberRepository, ReservationRepository reservationRepository, S3Uploader s3Uploader) {
        this.memberRepository = memberRepository;
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.s3Uploader = s3Uploader;
    }


    public Review createReview(ReviewSaveDto dto, MultipartFile image) {
        // 현재 인증된 사용자의 이메일을 가져옴
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 예약입니다."));

        if (reviewRepository.findByReservationAndDelYN(reservation, DelYN.N).isPresent()) {
            throw new IllegalStateException("이미 작성된 리뷰가 있습니다.");
        }

        String imagePath = null;
        try {
            if (image != null && !image.isEmpty()) {
                imagePath = s3Uploader.upload(image);
            }
        } catch (IOException e) {
            throw new IllegalStateException("이미지 업로드에 실패했습니다.", e);
        }

        Review review = dto.toEntity(member, reservation, imagePath);
        return reviewRepository.save(review);
    }
}
