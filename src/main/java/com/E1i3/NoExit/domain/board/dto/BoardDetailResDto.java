package com.E1i3.NoExit.domain.board.dto;

import com.E1i3.NoExit.domain.board.domain.BoardType;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardDetailResDto {
    private Long id; // 아이디
    private String writer; // 작성자
    private String title; //  제목
    private String content; // 내용
    private int boardHits; // 조회수
    private int likes; // 좋아요
    private int dislikes; // 싫어요
    private List<Comment> comments; // 댓글
    private LocalDateTime createdTime; // 작성시간
    private LocalDateTime updatedTime; // 수정시간
    private String imagePath; // 이미지
    private BoardType boardType; // 게시판 유형 (자유, 전략)
}
