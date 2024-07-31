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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class ReviewService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    public ReviewService(S3Client s3Client, ReviewRepository reviewRepository, MemberRepository memberRepository, ReservationRepository reservationRepository) {
        this.s3Client = s3Client;
        this.memberRepository = memberRepository;
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }


    @Transactional
    public Review createReview(ReviewSaveDto dto) {
        MultipartFile image = dto.getReviewImage();

        // 현재 인증된 사용자의 이메일을 가져옴
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 예약입니다."));


        if (reviewRepository.findByReservationAndDelYN(reservation, DelYN.N).isPresent()) {
            throw new IllegalStateException("이미 작성된 리뷰가 있습니다.");

        Review review = dto.toEntity(member, reservation);
        review = reviewRepository.save(review);

        try {
            if (image != null && !image.isEmpty()) {
                // 파일을 로컬 디스크에 저장
                byte[] bytes = image.getBytes();
                String fileName = review.getId() + "_" + image.getOriginalFilename();
                Path path = Paths.get("C:/springboot_img", fileName);
                Files.write(path, bytes);

                // S3에 파일 업로드
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .build();
                PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromFile(path));

                // 업로드된 파일의 S3 URL 가져오기
                String s3Path = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
                review.updateImagePath(s3Path);

                // 로컬 파일 삭제
                Files.delete(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패");
        }
        return review;
    }
}
