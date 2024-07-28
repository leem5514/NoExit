package com.E1i3.NoExit.domain.review.dto;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSaveDto {

    private String content;

    private int rating;

    private String imagePath;

    private Long memberId;

    private Long reservationId;

    public Review toEntity(Member member, Reservation reservation) {
        return Review.builder()
                .content(this.content)
                .rating(this.rating)
                .imagePath(this.imagePath)
                .member(member)
                .reservation(reservation)
                .build();
    }
}
