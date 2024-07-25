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
public class FindBoardUpdateReqDto {

    private String title;
    private String contents;
    private LocalDateTime expirationDate;
    private int totalCapacity;
    private byte[] image;

    public FindBoard toEntity(Long id, FindBoard existingBoard) {
        return FindBoard.builder()
                .id(id)
                .memberId(existingBoard.getMemberId())
                .title(title != null ? title : existingBoard.getTitle())
                .contents(contents != null ? contents : existingBoard.getContents())
                .createdAt(existingBoard.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .expirationDate(expirationDate != null ? expirationDate : existingBoard.getExpirationDate())
                .currentCount(existingBoard.getCurrentCount())
                .totalCapacity(totalCapacity != 0 ? totalCapacity : existingBoard.getTotalCapacity())
                .participantCount(existingBoard.getParticipantCount())
                .image(image != null ? image : existingBoard.getImage())
                .view(existingBoard.getView())
                .build();
    }
}
