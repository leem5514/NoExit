package com.E1i3.NoExit.domain.review.service;

import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.domain.ReservationStatus;
import com.E1i3.NoExit.domain.reservation.repository.ReservationRepository;
import com.E1i3.NoExit.domain.review.domain.Review;
import com.E1i3.NoExit.domain.review.dto.ReviewListDto;
import com.E1i3.NoExit.domain.review.dto.ReviewSaveDto;
import com.E1i3.NoExit.domain.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder.folderName2}")
    private String reviewFolder;

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


    /*  */
    @Transactional
    public Review createReview(ReviewSaveDto dto) {

        // 리뷰를 작성하기 위한 필요 조건(추가) (예약 state 가 ACCEPT 가 된 조건, 로그인 된 사용자 정보, 예약 일수가 2주가 지나지 않은 사용자)
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 예약입니다."));

        // 예약 상태가 ACCEPT인지 확인
        if (reservation.getReservationStatus() != ReservationStatus.ACCEPT) {
            throw new IllegalStateException("리뷰를 작성할 수 없는 상태입니다. 예약이 승인되지 않았습니다.");
        }

        // 예약 상태가 ACCEPT인 후 일주일이 지났는지 확인
        if (reservation.getResDate().isAfter(LocalDate.now().minusWeeks(1))) {
            throw new IllegalStateException("리뷰는 예약 상태가 ACCEPT된 후 일주일이 지난 후에만 작성할 수 있습니다.");
        }

        if (reviewRepository.findByReservationAndDelYN(reservation, DelYN.N).isPresent()) {
            throw new IllegalStateException("이미 작성된 리뷰가 있습니다.");
        }

        MultipartFile image = dto.getReviewImage();
        Review review = null;
        try {
            review = reviewRepository.save(dto.toEntity(member, reservation));
            // 파일을 로컬 디스크에 저장
            byte[] bytes = image.getBytes();
            String fileName = review.getId() + "_" + image.getOriginalFilename();
            Path path = Paths.get("C:/springboot_img", fileName);

            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            String s3Key = reviewFolder + fileName;
            // S3에 파일 업로드
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build();
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));

            // 업로드된 파일의 S3 URL 가져오기
            String s3Path = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(s3Key)).toExternalForm();
            review.updateImagePath(s3Path);

        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패");
        }
        return review;
    }

    /* 리뷰 리스트(전체 사용자 기준 전체 리스트) */
    public Page<ReviewListDto> pageReview(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByDelYN(DelYN.N, pageable);
        return reviews.map(Review::fromEntity);
    }

    /* 리뷰리스트(내가 쓴 글 리스트 ) */
    public Page<ReviewListDto> getUserReviews(Pageable pageable) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        Page<Review> reviews = reviewRepository.findByMemberAndDelYN(member, DelYN.N, pageable);
        return reviews.map(Review::fromEntity);
    }

}
