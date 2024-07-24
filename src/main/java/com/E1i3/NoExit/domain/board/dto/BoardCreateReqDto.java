package com.E1i3.NoExit.domain.board.dto;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.domain.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCreateReqDto {
    private Long member_id; // 작성자 멤버 아이디
    private String writer; // 작성자 > 이런 거 안 쓰면 에타처럼 익명1 이렇게 되게 해야 할까요
    private String title; //  제목 > 이런 거 안 쓰면 ㅇ월ㅇ일 ㅇ시ㅇ분에 작성된 글입니다. 디폴트로 되게 만들어야 할까요
    private String content; // 내용
    private String category; // 카테고리
    @Column(name = "image_path", nullable = false)
    private String image_path; // 이미지
    private BoardType board_type; // 게시판 유형 (자유, 전략)

    public Board toEntity() {
        Board board = Board.builder()
                .member_id(this.member_id)
                .writer(this.writer)
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .image_path(this.image_path)
                .board_type(this.board_type)
                .build();

        return board;
    }
}

