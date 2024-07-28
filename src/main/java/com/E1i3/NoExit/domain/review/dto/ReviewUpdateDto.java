package com.E1i3.NoExit.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateDto {
    private String content;
    private String rating;
    private String imagePath;
}
