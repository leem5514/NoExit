package com.E1i3.NoExit.domain.findboard.dto;

import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindBoardResDto {

    private Long member_id;
    private Long id; // 게시글 번호
    private String writer;
    private String title;
    private String contents;
    private LocalDateTime cratedTime;
    private LocalDateTime updateTime;
    private LocalDateTime expirationTime;
    private int currentCount;
    private int totalCapacity;
    private byte[] image;

}
