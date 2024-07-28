package com.E1i3.NoExit.domain.board.controller;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.dto.BoardCreateReqDto;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.board.service.BoardService;
import com.E1i3.NoExit.domain.common.dto.CommonErrorDto;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
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
@Api(tags="게시판 서비스")
public class BoardController {

    private final BoardService boardService;
    @Autowired
    public BoardController(BoardService boardService) {

        this.boardService = boardService;
    }

    @Operation(summary= "게시글 작성")
    @PostMapping("/board/create") // 게시글 생성
    public ResponseEntity<Object> boardCreate(@RequestBody BoardCreateReqDto dto) {
        try {
            boardService.boardCreate(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "board is successfully created", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }





    @Operation(summary= "게시글 전체 조회")
    @GetMapping("/board/list") // 게시글 전체 조회
    public ResponseEntity<Object> boardRead(
            @PageableDefault(size=10,sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardListResDto> dtos =  boardService.boardList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "list is successfully found", dtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }






    @Operation(summary= "게시글 상세 조회")
    @GetMapping("/board/detail/{id}") // 게시글 상세조회
    public ResponseEntity<?> boardDetailRead(@PathVariable Long id) {
        BoardDetailResDto dto = boardService.boardDetail(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "board is successfully found", dto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }






    @Operation(summary= "게시글 수정")
    @PostMapping("/board/update/{id}") // 게시글 수정
    public ResponseEntity<Object> boardUpdate(@PathVariable Long id, @RequestBody BoardUpdateReqDto dto) {
        try {
            boardService.boardUpdate(id, dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "board is successfully updated", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }







//    @DeleteMapping("/board/delete/{id}") // 게시글 삭제
//    public String boardDelete(@PathVariable Long id) {
//        boardService.boardDelete(id);
//        return "ok";
//    }

    @Operation(summary= "게시글 삭제")
    @PostMapping("/board/delete/{id}") // 게시글 삭제
    public ResponseEntity<Object> boardDelete(@PathVariable Long id) {
        try {
            boardService.boardDelete(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "board is successfully deleted", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary= "게시글 좋아요")
    @PostMapping("/board/like/{id}")
    public ResponseEntity<Object> boardLike(@PathVariable Long id) {
        try {
            int likes = boardService.boardUpdateLikes(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "comment is successfully deleted", likes);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary= "게시글 싫어요")
    @PostMapping("/board/dislike/{id}")
    public ResponseEntity<Object> boardDislike(@PathVariable Long id) {
        try {
            int dislikes = boardService.boardUpdateDislikes(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "comment is successfully deleted", dislikes);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
