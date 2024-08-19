package com.E1i3.NoExit.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateDto {
    private String content;
    private int rating;
    private MultipartFile reviewImage;
}
