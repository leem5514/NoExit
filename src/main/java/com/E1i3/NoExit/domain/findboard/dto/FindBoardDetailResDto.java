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
public class FindBoardDetailResDto {

    private Long id;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expirationDate;
    private int currentCount;
    private int totalCapacity;
    private int participantCount;
    private byte[] image;
    private String view;
    private Long memberId;

    // 엔티티에서 변환하는 메서드
    public static FindBoardDetailResDto fromEntity(FindBoard findBoard) {
        return FindBoardDetailResDto.builder()
                .id(findBoard.getId())
                .title(findBoard.getTitle())
                .contents(findBoard.getContents())
                .createdAt(findBoard.getCreatedAt())
                .updatedAt(findBoard.getUpdatedAt())
                .expirationDate(findBoard.getExpirationDate())
                .currentCount(findBoard.getCurrentCount())
                .totalCapacity(findBoard.getTotalCapacity())
                .participantCount(findBoard.getParticipantCount())
                .image(findBoard.getImage())
                .view(findBoard.getView())
                .memberId(findBoard.getMemberId())
                .build();
    }
}
