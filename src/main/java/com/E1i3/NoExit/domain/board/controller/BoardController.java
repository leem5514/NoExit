package com.E1i3.NoExit.domain.board.controller;

import com.E1i3.NoExit.domain.board.dto.BoardCreateReqDto;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
public class BoardController {

    private final BoardService boardService;
    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/board/create") // 게시글 생성
    public String boardCreate(@RequestBody BoardCreateReqDto dto) {
        boardService.boardCreate(dto);
        return "ok";
    }





    @GetMapping("/board/list") // 게시글 전체 조회
    public Page<BoardListResDto> boardRead(
            @PageableDefault(size=10,sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return boardService.boardList(pageable);
    }






    @GetMapping("/board/detail/{id}") // 게시글 상세조회
    public BoardDetailResDto boardDetailRead(@PathVariable Long id) {
        return boardService.boardDetail(id);
    }







    @PostMapping("/board/update/{id}") // 게시글 수정
    public String boardUpdate(@PathVariable Long id, @RequestBody BoardUpdateReqDto dto) {
        boardService.boardUpdate(id, dto);
        return "ok";
    }







    @DeleteMapping("/board/delete/{id}") // 게시글 삭제
    public String boardDelete(@PathVariable Long id) {
        boardService.boardDelete(id);
        return "ok";
    }
}
