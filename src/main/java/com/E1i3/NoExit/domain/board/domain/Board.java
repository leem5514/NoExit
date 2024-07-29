package com.E1i3.NoExit.domain.board.domain;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.common.domain.BaseTimeEntity;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 아이디
//    private Long memberId; // 작성자 아이디

//    private String writer; // 작성자 // 굳이 필요할까...

    @Column(nullable = false)
    private String title; //  제목

    private String content; // 내용

    private int boardHits; // 조회수
    private int likes; // 좋아요
    private int dislikes; // 싫어요

    @ManyToOne //추가 7-25
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "imagePath")
    private String imagePath; // 이미지

    private int notificationState; // 알림 상태

    @Enumerated(EnumType.STRING)
    private BoardType boardType; // 게시판 유형 (자유, 공략)

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>(); // 게시글에 달린 댓글들

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N; // 삭제여부


    public BoardListResDto fromEntity(){
        BoardListResDto boardListResDto = BoardListResDto.builder()
                .writer(this.member.getNickname())
                .title(this.title)
                .boardHits(this.boardHits)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .boardType(this.boardType)
                .build();

        return boardListResDto;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public BoardDetailResDto detailFromEntity(){
        BoardDetailResDto boardDetailResDto = BoardDetailResDto.builder()
                .id(this.id)
                .writer(this.member.getNickname()) //
                .title(this.title)
                .content(this.content)
                .boardHits(this.boardHits)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .imagePath(this.imagePath)
                .boardType(this.boardType)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdateTime())
                .build();

        return boardDetailResDto;
    }

    public void updateEntity(BoardUpdateReqDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.imagePath = dto.getImagePath();
        this.boardType = dto.getBoardType();
    }


    public void deleteEntity() {
        this.delYN = DelYN.Y;
    }

    public void updateBoardHits() {
        this.boardHits++;
    }

    public void updateLikes() {
        this.likes++;
    }

    public void updateDislikes() {
        this.dislikes++;
    }
}
