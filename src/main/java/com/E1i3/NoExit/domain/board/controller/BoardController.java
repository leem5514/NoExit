package com.E1i3.NoExit.domain.board.controller;

import com.E1i3.NoExit.domain.board.dto.BoardCreateReqDto;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
public class BoardController {

    private final BoardService boardService;
    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }




    @PostMapping("/board/create") // 게시글 생성
    public String boardCreate(@ModelAttribute BoardCreateReqDto dto) {
        boardService.boardCreate(dto);
        return "redirect:/board/list";
    }





    @GetMapping("/board/list")
    public List<BoardListResDto> boardRead() {
        return boardService.boardList();
    }




    @GetMapping("/board/detail/{id}")
    public BoardDetailResDto boardDetailList(@PathVariable Long id) {
        return boardService.boardDetail(id);
    }



    @PatchMapping("/board/update")
    public String boardUpdate(@RequestBody BoardUpdateReqDto dto) {
        boardService.boardUpdate(dto);
        return "ok";
    }




    @DeleteMapping("/board/delete/{id}")
    public String boardDelete(@PathVariable Long id) {
        boardService.boardDelete(id);
        return "ok";
    }










}
