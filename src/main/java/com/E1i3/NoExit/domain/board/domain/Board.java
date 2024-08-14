package com.E1i3.NoExit.domain.board.domain;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.comment.dto.CommentListResDto;
import com.E1i3.NoExit.domain.common.domain.BaseTimeEntity;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.*;
import org.apache.ibatis.annotations.One;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

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

    @ManyToOne //추가 7-25
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title; //  제목

    private String contents; // 내용

    private int boardHits; // 조회수

    private int likes; // 좋아요 수
    private int dislikes; // 싫어요 수

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<BoardImage> imgs = new ArrayList<>(); // 이미지

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
                .id(this.id)
                .writer(this.member.getNickname())
                .title(this.title)
                .boardHits(this.boardHits)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .comments(this.comments.size())
                .thumbnail(this.imgs.get(0).getImageUrl())
                .boardType(this.boardType)
                .build();

        return boardListResDto;
    }



    public BoardDetailResDto detailFromEntity(){

        List<Comment> comments = this.getComments();
        List<CommentListResDto> dtos = new ArrayList<>();
        for(Comment c : comments) {
            if(c.getDelYN().equals(DelYN.Y)) {
                continue;
            }
            dtos.add(c.fromEntity());
        }
        BoardDetailResDto boardDetailResDto = BoardDetailResDto.builder()
                .id(this.id)
                .writer(this.member.getNickname()) //
                .title(this.title)
                .contents(this.contents)
                .boardHits(this.boardHits)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .boardType(this.boardType)
                .comments(dtos)
                .createdDate(this.getCreatedTime().toLocalDate())
                .build();

        return boardDetailResDto;
    }

    public void updateEntity(BoardUpdateReqDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.boardType = dto.getBoardType();
    }

    public void deleteEntity() {
        this.delYN = DelYN.Y;
    }

    public void updateBoardHits() {
        this.boardHits++;
    }

    public void updateLikes(boolean like) {
        if(like) {
            this.likes++;
        }else{
            this.likes--;
        }
    }

    public void updateDislikes(boolean dislike) {
        if(dislike) {
            this.dislikes++;
        }else{
            this.dislikes--;
        }
    }

}
