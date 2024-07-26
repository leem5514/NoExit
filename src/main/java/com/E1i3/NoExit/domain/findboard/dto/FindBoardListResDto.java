package com.E1i3.NoExit.domain.findboard.dto;

import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import lombok.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindBoardListResDto {

    private Long member_id;
    private Long id;
    private String writer;
    private String title;
    private String contents;
    private LocalDateTime expirationTime;
    private int totalCapacity;
    private LocalDateTime cratedTime;
    private int currentCount;
    private byte[] image;

}
