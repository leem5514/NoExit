package com.E1i3.NoExit.domain.board.dto;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.domain.BoardType;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCreateReqDto {
    private Long memberId; // 작성자 멤버 아이디
    private String writer; // 작성자 > 익명 처리 등을 위해
    private String title; // 제목 > 디폴트 값 처리 필요 시 추가
    private String content; // 내용
    private String imagePath; // 이미지
    private BoardType boardType; // 게시판 유형 (자유, 전략)

    public Board toEntity(Member member) {
        Board board = Board.builder()
                .member(member) // member를 설정
                .writer(this.writer)
                .title(this.title)
                .content(this.content)
                .imagePath(this.imagePath)
                .boardType(this.boardType)
                .build();

        return board;
    }
}
