package com.E1i3.NoExit.domain.comment.dto;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.comment.domain.DelYN;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentListResDto {

//    private Long boardId; // 댓글 단 게시글 아이디
//    private Board board;

    private Long memberId; // 댓글 작성자 아이디

    private String content; // 댓글 내용

    private int likes; // 좋아요 수

    private int dislikes; // 싫어요 수

    private LocalDateTime createdTime; // 작성시간

    private LocalDateTime updatedTime; // 수정시간

}
