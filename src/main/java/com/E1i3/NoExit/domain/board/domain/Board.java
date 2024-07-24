package com.E1i3.NoExit.domain.board.domain;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 아이디

    @Column(nullable = false)
    private Long member_id; // 작성자 아이디

    private String writer; // 작성자 > 이런 거 안 쓰면 에타처럼 익명1 이렇게 되게 해야 할까요

    private String title; //  제목 > 이런 거 안 쓰면 ㅇ월ㅇ일 ㅇ시ㅇ분에 작성된 글입니다. 디폴트로 되게 만들어야 할까요

    private String content; // 내용

    private String category; // 카테고리

    @Column(nullable = false)
    private int board_hits; // 조회수

    @Column(nullable = false)
    private int likes; // 좋아요

    private int dislikes; // 싫어요

    private LocalDateTime created_at; // 작성시간

    private LocalDateTime update_at; // 수정시간

    @Column(name = "image_path", nullable = false)
    private String image_path; // 이미지

    private int notification_state; // 알림 상태

    @Enumerated(EnumType.STRING)
    private BoardType board_type; // 게시판 유형 (자유, 전략)


    public BoardListResDto fromEntity(){
        BoardListResDto boardListResDto = BoardListResDto.builder()
                .writer(this.writer)
                .title(this.title)
                .category(this.category)
                .board_hits(this.board_hits)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .created_at(this.created_at)
                .update_at(this.update_at)
                .board_type(this.board_type)
                .build();

        return boardListResDto;
    }

    public BoardDetailResDto detailFromEntity(){
        BoardDetailResDto boardDetailResDto = BoardDetailResDto.builder()
                .id(this.id)
                .writer(this.writer)
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .board_hits(this.board_hits)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .created_at(this.created_at)
                .update_at(this.update_at)
                .image_path(this.image_path)
                .board_type(this.board_type)
                .build();

        return boardDetailResDto;
    }

    public void updateBoard(BoardUpdateReqDto boardUpdateReqDto) {
        this.title = boardUpdateReqDto.getTitle();
        this.content = boardUpdateReqDto.getContents();
    }

}
