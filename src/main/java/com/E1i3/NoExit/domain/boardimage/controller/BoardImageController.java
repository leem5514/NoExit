package com.E1i3.NoExit.domain.boardimage.controller;


import com.E1i3.NoExit.domain.boardimage.service.BoardImageService;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BoardImageController {
    private final BoardImageService boardImageService;

    @Autowired
    public BoardImageController(BoardImageService boardImageService) {
        this.boardImageService = boardImageService;
    }

    // 사진 추가
    @PostMapping("/boardimg/add/{boardId}")
    public ResponseEntity<?> addImage(@PathVariable Long boardId, @RequestPart MultipartFile imgFile) {
        boardImageService.addImg(boardId, imgFile);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "사진 추가", null);
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }


    // 사진 삭제
    @PatchMapping("/boardimg/delete/{imgId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imgId) {
        boardImageService.deleteImg(imgId);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "사진 삭제", null);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

}
