package com.E1i3.NoExit.domain.comment.dto;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.comment.domain.DelYN;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateReqDto {
    private Board board; // 댓글 단 게시글 아이디
    private Long memberId; // 댓글 작성자 아이디
    private String content; // 댓글 내용


    public Comment toEntity() {
        Comment comment = Comment.builder()

                .build();

        return comment;
    }
}

