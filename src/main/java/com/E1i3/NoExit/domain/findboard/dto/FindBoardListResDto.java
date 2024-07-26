package com.E1i3.NoExit.domain.findboard.dto;


import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindBoardListResDto {

    private String writer;
    private String title;
    private String contents;
    private LocalDateTime expirationDate;
    private int totalCapacity;
    private LocalDateTime createdAt;
    private int currentCount;
    private byte[] image;

    public static FindBoardListResDto fromEntity(FindBoard findBoard) {
        return FindBoardListResDto.builder()
                .writer(findBoard.getWriter())
                .title(findBoard.getTitle())
                .contents(findBoard.getContents())
                .expirationDate(findBoard.getExpirationDate())
                .totalCapacity(findBoard.getTotalCapacity())
                .createdAt(findBoard.getCreatedAt())
                .currentCount(findBoard.getCurrentCount())
                .image(findBoard.getImage())
                .build();
    }

}
