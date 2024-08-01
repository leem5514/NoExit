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
    @ManyToOne //추가 7-25
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title; //  제목

    private String content; // 내용

    private int boardHits; // 조회수

    private int likes; // 좋아요 수
    private int dislikes; // 싫어요 수

//    @Builder.Default
//    private List<String> likeMembers = new ArrayList<>(); // 좋아요 누른 회원들

//    @Builder.Default
//    private List<String> dislikeMembers = new ArrayList<>(); // 싫어요 누른 회원들

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
//                .likes(this.likeMembers.size())
//                .dislikes(this.dislikeMembers.size())
                .boardType(this.boardType)
                .build();

        return boardListResDto;
    }



    public BoardDetailResDto detailFromEntity(){

        List<Comment> comments = this.getComments();
        List<CommentListResDto> dtos = new ArrayList<>();
        for(Comment c : comments) {
            if(c.getDelYN().equals(com.E1i3.NoExit.domain.comment.domain.DelYN.Y)) {
                continue;
            }
            dtos.add(c.fromEntity());
        }
        BoardDetailResDto boardDetailResDto = BoardDetailResDto.builder()
                .id(this.id)
                .writer(this.member.getNickname()) //
                .title(this.title)
                .content(this.content)
                .boardHits(this.boardHits)
                .likes(this.likes)
                .dislikes(this.dislikes)
//                .likes(this.likeMembers.size())
//                .dislikes(this.dislikeMembers.size())
                .imagePath(this.imagePath)
                .boardType(this.boardType)
                .comments(dtos)
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

    /*
    // 멤버로 받을 때 함수
    public void updateLikes(String email) {
        try {
            for (String s : this.dislikeMembers) {
                if (email.equals(s)) {
                    throw new IllegalArgumentException("이미 싫어요를 누른 게시글입니다.");
                }
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        try{
            for(String s : this.likeMembers) {
                if(email.equals(s)) {
                    this.likeMembers.remove(s);
                    return;
                }
            }
        } catch (NullPointerException e) {
            this.likeMembers.add(email);
        }
    }

    public void updateDislikes(String email) {
        try {
            for (String s : this.likeMembers) {
                if (email.equals(s)) {
                    throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
                }
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        try{
            for(String s : this.dislikeMembers) {
                if(email.equals(s)) {
                    this.dislikeMembers.remove(s);
                    return;
                }
            }
        } catch (NullPointerException e) {
            this.dislikeMembers.add(email);
        }
    }*/
}
