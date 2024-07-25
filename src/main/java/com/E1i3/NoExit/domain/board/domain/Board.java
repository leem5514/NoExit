package com.E1i3.NoExit.domain.board.domain;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 아이디

    private Long memberId; // 작성자 아이디

    private String writer; // 작성자 > 이런 거 안 쓰면 에타처럼 익명1 이렇게 되게 해야 할까요

    private String title; //  제목 > 이런 거 안 쓰면 ㅇ월ㅇ일 ㅇ시ㅇ분에 작성된 글입니다. 디폴트로 되게 만들어야 할까요

    private String content; // 내용

    private int boardHits; // 조회수

    private int likes; // 좋아요

    private int dislikes; // 싫어요

    @CreationTimestamp
    private LocalDateTime createdTime; // 작성시간

    @UpdateTimestamp
    private LocalDateTime updatedTime; // 수정시간

    @Column(name = "imagePath")
    private String imagePath; // 이미지

    private int notificationState; // 알림 상태

    @Enumerated(EnumType.STRING)
    private BoardType boardType; // 게시판 유형 (자유, 공략)

    private DelYN delYN;


    public BoardListResDto fromEntity(){
        BoardListResDto boardListResDto = BoardListResDto.builder()
                .writer(this.writer)
                .title(this.title)
//                .category(this.category)
                .boardHits(this.boardHits)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .createdTime(this.createdTime)
                .updatedTime(this.updatedTime)
                .boardType(this.boardType)
                .build();

        return boardListResDto;
    }

    public BoardDetailResDto detailFromEntity(){
        BoardDetailResDto boardDetailResDto = BoardDetailResDto.builder()
                .id(this.id)
                .writer(this.writer)
                .title(this.title)
                .content(this.content)
//                .category(this.category)
                .boardHits(this.boardHits)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .createdTime(this.createdTime)
                .updatedTime(this.updatedTime)
                .imagePath(this.imagePath)
                .boardType(this.boardType)
                .build();

        return boardDetailResDto;
    }

    public void updateEntity(BoardUpdateReqDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.imagePath = dto.getImagePath();
        this.boardType = dto.getBoardType();
    }

}
