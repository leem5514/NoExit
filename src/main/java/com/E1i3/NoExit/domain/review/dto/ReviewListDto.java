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
    private String content;
    private int rating;
    private String imagePath;

    public static ReviewListDto listFromEntity(Review review) {
        return ReviewListDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .rating(review.getRating())
                .imagePath(review.getImagePath())
                .build();
    }
}
