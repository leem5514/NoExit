package com.E1i3.NoExit.domain.findboard.dto;

import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FindBoardUpdateReqDto {
    private String writer;
    private String title;
    private String contents;
    private LocalDateTime expirationDate;
    private int totalCapacity;
    private byte[] image;

    public FindBoard toEntity(Long id, FindBoard existingBoard) {
        existingBoard.updateFromDto(this);
        return existingBoard;
    }
}
