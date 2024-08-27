package com.E1i3.NoExit.domain.boardimage.domain;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.boardimage.dto.BoardImageDto;
import com.E1i3.NoExit.domain.common.domain.BaseTimeEntity;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public void deleteEntity() {
        this.delYN = DelYN.Y;
    }

    public BoardImageDto fromEntity(){
        BoardImageDto dto = BoardImageDto.builder()
                .id(this.id)
                .imageUrl(this.imageUrl)
                .delYN(this.delYN)
                .build();
        return dto;
    }
}