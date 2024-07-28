package com.E1i3.NoExit.domain.findboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class FindBoardUpdateReqDto {
    private String title;
    private String contents;
    private LocalDateTime expirationDate;
    private int totalCapacity;
    private byte[] image;

    public FindBoardUpdateReqDto(String title, String contents, LocalDateTime expirationDate, int totalCapacity, byte[] image) {
        this.title = title;
        this.contents = contents;
        this.expirationDate = expirationDate;
        this.totalCapacity = totalCapacity;
        this.image = image;
    }
}
