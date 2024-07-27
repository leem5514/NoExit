package com.E1i3.NoExit.domain.comment.domain;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.comment.dto.CommentListResDto;
import com.E1i3.NoExit.domain.comment.dto.CommentUpdateReqDto;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 아이디

    @ManyToOne
    @JoinColumn(name = "board_id")
//    private Long boardId; // 댓글 단 게시글 아이디
    private Board board;

    private Long memberId; // 댓글 작성자 아이디 // 로그인하면 필요없음

    private String content; // 댓글 내용

    private int likes; // 좋아요 수

    private int dislikes; // 싫어요 수

    @CreationTimestamp
    private LocalDateTime createdTime; // 작성시간

    @UpdateTimestamp
    private LocalDateTime updatedTime; // 수정시간

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N; // 삭제 여부


    public CommentListResDto fromEntity(){
        CommentListResDto commentListResDto = CommentListResDto.builder()
//                .board(this.board)
                .memberId(this.memberId)
                .content(this.content)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .createdTime(this.createdTime)
                .updatedTime(this.updatedTime)
                .build();

        return commentListResDto;
    }

    public void updateEntity(CommentUpdateReqDto dto) {
        this.content = dto.getContent();
    }

    public void deleteEntity() {
        this.delYN = DelYN.Y;
    }

    public void updateLikes() {
        this.likes++;
    }

    public void updateDislikes() {
        this.dislikes++;
    }

}
