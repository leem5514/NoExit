package com.E1i3.NoExit.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentListResDto {

    private Long id;
    private String writer; // 댓글 작성자 닉네임
    private String contents; // 댓글 내용
    private int likes; // 좋아요 수
    private int dislikes; // 싫어요 수
    private LocalDateTime createdTime; // 작성시간
}
