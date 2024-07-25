package com.E1i3.NoExit.domain.findboard.dto;

import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindBoardResDto {

    private String title;
    private String contents;
    private LocalDateTime expirationDate; // 카멜 케이스로 수정
    private int currentCount; // 카멜 케이스로 수정
    private int totalCapacity; // 카멜 케이스로 수정
    private byte[] image;

    // 엔티티에서 변환하는 메서드
    public static FindBoardResDto fromEntity(FindBoard findBoard) {
        return FindBoardResDto.builder()
                .title(findBoard.getTitle())
                .contents(findBoard.getContents())
                .expirationDate(findBoard.getExpirationDate())
                .currentCount(findBoard.getCurrentCount())
                .totalCapacity(findBoard.getTotalCapacity())
                .image(findBoard.getImage())
                .build();
    }
}
