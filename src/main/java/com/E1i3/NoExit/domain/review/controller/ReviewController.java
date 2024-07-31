package com.E1i3.NoExit.domain.review.controller;

import com.E1i3.NoExit.domain.common.auth.JwtTokenProvider;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.review.domain.Review;
import com.E1i3.NoExit.domain.review.dto.ReviewSaveDto;
import com.E1i3.NoExit.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

}
