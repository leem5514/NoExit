package com.E1i3.NoExit.domain.review.dto;

import com.E1i3.NoExit.domain.reservation.dto.ReservationListResDto;
import com.E1i3.NoExit.domain.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListDto {
    private Long id;
    private int rating;
    private String imagePath;
    private String content;
    private String memberNickname; // 작성자 닉네임
    private String gameName;
}
