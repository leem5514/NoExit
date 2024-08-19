package com.E1i3.NoExit.domain.board.dto;

import com.E1i3.NoExit.domain.board.domain.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardUpdateReqDto {
    private String title; // 제목
    private String contents; // 내용
    private List<Long> removeImgs;
    private BoardType boardType; // 게시판 유형
}