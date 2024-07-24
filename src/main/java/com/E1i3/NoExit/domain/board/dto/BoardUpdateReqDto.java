package com.E1i3.NoExit.domain.board.dto;

import com.E1i3.NoExit.domain.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardUpdateReqDto {

    private String title;
    private String content;

    public BoardUpdateReqDto toEntity(Board board){
        BoardUpdateReqDto boardUpdateReqDto = BoardUpdateReqDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
        return boardUpdateReqDto;
    }
}