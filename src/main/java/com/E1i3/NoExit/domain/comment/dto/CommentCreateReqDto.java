package com.E1i3.NoExit.domain.comment.dto;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateReqDto {
    private Long boardId; // 댓글 단 게시글 아이디
//    private Long memberId; // 댓글 작성자 아이디
    private Long memberId; // 로그인 하면 받아올 필요 없음
    private String content; // 댓글 내용


    public Comment toEntity(Board board) {
        Comment comment = Comment.builder()
                .board(board)
                .content(this.content)
                .build();
        return comment;
    }

}

