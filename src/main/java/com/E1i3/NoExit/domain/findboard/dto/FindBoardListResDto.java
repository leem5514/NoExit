package com.E1i3.NoExit.domain.findboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindBoardListResDto {

    private Long member_id;
    private Long id;
    private String writer;
    private String title;
    private String contents;
    private LocalDateTime createdTime;
    private LocalDateTime expirationTime;
    private int totalCapacity;
    private int currentCount;
    private byte[] image;

}
