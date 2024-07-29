package com.E1i3.NoExit.domain.comment.controller;

import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.comment.dto.CommentCreateReqDto;
import com.E1i3.NoExit.domain.comment.dto.CommentListResDto;
import com.E1i3.NoExit.domain.comment.dto.CommentUpdateReqDto;
import com.E1i3.NoExit.domain.comment.service.CommentService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags="댓글 서비스")
public class CommentController {

    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // url은 임의로 대충 붙임


    @Operation(summary= "댓글 작성")
    @PostMapping("/comment/create") // 댓글 생성
    public ResponseEntity<Object> commentCreate(@RequestBody CommentCreateReqDto dto) {
        try {
            commentService.commentCreate(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "comment is successfully created", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }





    @Operation(summary= "댓글 조회")
    @GetMapping("/comment/list") // 댓글 조회
    public ResponseEntity<Object> commentRead(
            @PageableDefault(size=10,sort = "createdTime", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CommentListResDto> dtos = commentService.commentList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "list is successfully found", dtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }









    @Operation(summary= "댓글 수정")
    @PatchMapping("/comment/update/{id}") // 댓글 수정
    public ResponseEntity<Object> commentUpdate(@PathVariable Long id, @RequestBody CommentUpdateReqDto dto) {
        try {
            commentService.commentUpdate(id, dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "comment is successfully updated", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }





    @Operation(summary= "댓글 삭제")
    @PatchMapping("/comment/delete/{id}") // 댓글 삭제
    public ResponseEntity<Object> commentDelete(@PathVariable Long id) {
        try {
            commentService.commentDelete(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "comment is successfully deleted", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary= "댓글 좋아요")
    @PostMapping("/comment/like/{id}")
    public ResponseEntity<Object> commentLike(@PathVariable Long id) {
        try {
            int likes = commentService.commentUpdateLikes(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "comment is successfully deleted", likes);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary= "댓글 싫어요")
    @PostMapping("/comment/dislike/{id}")
    public ResponseEntity<Object> commentDislike(@PathVariable Long id) {
        try {
            int dislikes = commentService.commentUpdateDislikes(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "comment is successfully deleted", dislikes);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

}
