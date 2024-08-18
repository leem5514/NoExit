package com.E1i3.NoExit.domain.review.controller;

import com.E1i3.NoExit.domain.common.auth.JwtTokenProvider;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.review.domain.Review;
import com.E1i3.NoExit.domain.review.dto.ReviewListDto;
import com.E1i3.NoExit.domain.review.dto.ReviewSaveDto;
import com.E1i3.NoExit.domain.review.dto.ReviewUpdateDto;
import com.E1i3.NoExit.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags="리뷰 서비스")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {

        this.reviewService = reviewService;

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/review/create")
    @Operation(summary= "[일반 사용자] 리뷰 생성 API")
    public ResponseEntity<?> createReview(ReviewSaveDto dto) {
        Review review = reviewService.createReview(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "리뷰 작성이 완료되었습니다.", review.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @GetMapping("/review/all")
    @Operation(summary = "[전체 사용자] 리뷰 목록 조회 API")
    public ResponseEntity<CommonResDto> getReviews(
            @RequestParam(required = false) Long gameId, // 게임 ID를 쿼리 파라미터로 받음
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<ReviewListDto> reviews;

        if (gameId != null) {
            // 특정 게임 ID가 주어졌을 때 해당 게임의 리뷰만 조회
            reviews = reviewService.getReviewsByGameId(gameId, pageable);
        } else {
            // 게임 ID가 없으면 모든 리뷰 조회
            reviews = reviewService.pageReview(pageable);
        }

        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "리뷰 목록 조회가 완료되었습니다.", reviews);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/review/all/{gameId}")
    @Operation(summary = "[전체 사용자] 특정 게임 리뷰 목록 조회 API")
    public ResponseEntity<CommonResDto> getReviewsByGameId(
            @PathVariable Long gameId, // 경로 변수로 게임 ID를 받음
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<ReviewListDto> reviews = reviewService.getReviewsByGameId(gameId, pageable);

        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "리뷰 목록 조회가 완료되었습니다.", reviews);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/review/myall")
    @Operation(summary= "[일반 사용자] 리뷰 목록 조회 API")
    public ResponseEntity<?> getUserReviews(Pageable pageable) {
        Page<ReviewListDto> reviews = reviewService.getUserReviews(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "리뷰 목록 조회가 완료되었습니다.", reviews);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/review/delete/{id}")
    @Operation(summary = "[일반 사용자] 리뷰 삭제 API")
    public ResponseEntity<CommonResDto> deleteMyReview(@PathVariable Long id) {
        Review canceledReservation = reviewService.cancelReview(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "후기 삭제가 완료되었습니다.", canceledReservation.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('USER')")
//    @PostMapping("/review/update")
//    @Operation(summary= "[일반 사용자] 리뷰 수정 API")
//    public ResponseEntity<?> updateReview(@RequestBody ReviewUpdateDto dto) {
//        Review updatedReview = reviewService.updateReview(dto);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "리뷰 수정이 완료되었습니다.", updatedReview.getId());
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }


    // 리뷰 숫자 카운팅
    @GetMapping("/review/count")
    public long getReviewCount(@RequestParam Long gameId) {
        return reviewService.getReviewCountForGame(gameId);
    }


    /* 게임별 리뷰 개수와 평균 평점을 반환하는 엔드포인트 */
    @GetMapping("/review/game/{gameId}")
    public ResponseEntity<Map<String, Object>> getReviewsByGameId(@PathVariable Long gameId) {
        Map<String, Object> response = new HashMap<>();
        long reviewCount = reviewService.getReviewCountForGame(gameId);
        double averageRating = reviewService.calculateAverageRatingForGame(gameId);

        response.put("reviewCount", reviewCount);
        response.put("averageRating", averageRating);

        return ResponseEntity.ok(response);
    }
}
