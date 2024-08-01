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
    private String writer; // 추가된 필드
    private String contents;
    private LocalDateTime expirationTime; //이건 프론트에서 넘어오는 데이터 아닌가?

    private int totalCapacity;
    private byte[] image;
    //여기서 Role을 정해줘야하나? 아니지 role은 회원가입때 정해서 넘어온다.


    public FindBoard toEntity(Member member) {
        return FindBoard.builder()
                .title(this.title)
                .writer(member.getNickname()) // writer 필드 설정
                .contents(this.contents)
                .expirationTime(this.expirationTime)
                .totalCapacity(this.totalCapacity)
                .image(this.image)
                .member(member)
                .build();
    }
}
