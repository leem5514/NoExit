package com.E1i3.NoExit.domain.board.dto;

import com.E1i3.NoExit.domain.board.domain.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardListResDto {
    private Long id;
    private String writer; // 작성자
    private String title; //  제목
    private int boardHits; // 조회수
    private int likes; // 좋아요
    private int dislikes; // 싫어요
    private int comments; // 댓글수
    private BoardType boardType; // 게시판 유형 (자유, 전략)
    private String thumbnail;
    private LocalDate createdDate;

}
