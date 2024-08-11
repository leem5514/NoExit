package com.E1i3.NoExit.domain.findboard.dto;

import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindBoardSaveReqDto {

    private String title;
    private String writer;
    private String contents;
    private LocalDateTime expirationTime;

    private int totalCapacity;
    private String imagePath;

    public FindBoard toEntity(Member member) {
        return FindBoard.builder()
                .title(this.title)
                .writer(member.getNickname()) // writer 필드 설정
                .contents(this.contents)
                .expirationTime(this.expirationTime)
                .totalCapacity(this.totalCapacity)
                .imagePath(member.getProfileImage()) // 수정
                .member(member)
                .build();
    }
}
