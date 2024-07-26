package com.E1i3.NoExit.domain.comment.controller;

import com.E1i3.NoExit.domain.comment.dto.CommentCreateReqDto;
import com.E1i3.NoExit.domain.comment.dto.CommentListResDto;
import com.E1i3.NoExit.domain.comment.dto.CommentUpdateReqDto;
import com.E1i3.NoExit.domain.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // url은 임의로 대충 붙임



    @PostMapping("/comment/create") // 댓글 생성
    public String commentCreate(@RequestBody CommentCreateReqDto dto) {
        commentService.commentCreate(dto);
        return "ok";
    }





    @GetMapping("/comment/list") // 댓글 조회
    public Page<CommentListResDto> commentRead(
            @PageableDefault(size=10,sort = "createdTime", direction = Sort.Direction.ASC) Pageable pageable) {
        return commentService.commentList(pageable);
    }




    @PostMapping("/comment/update/{id}") // 댓글 수정
    public String boardUpdate(@PathVariable Long id, @RequestBody CommentUpdateReqDto dto) {
        commentService.commentUpdate(id, dto);
        return "ok";
    }





    @DeleteMapping("/comment/delete/{id}") // 댓글 삭제
    public String commentDelete(@PathVariable Long id) {
        commentService.commentDelete(id);
        return "ok";
    }
}
