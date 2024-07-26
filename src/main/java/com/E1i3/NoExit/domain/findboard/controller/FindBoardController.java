package com.E1i3.NoExit.domain.findboard.controller;

import com.E1i3.NoExit.domain.common.CommonResDto;
import com.E1i3.NoExit.domain.findboard.dto.*;
import com.E1i3.NoExit.domain.findboard.service.FindBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FindBoardController {

    private final FindBoardService findBoardService;

    @Autowired
    public FindBoardController(FindBoardService findBoardService) {
        this.findBoardService = findBoardService;
    }

    @PostMapping("/findboard/create")
    public ResponseEntity<Object> findBoardCreate(@RequestBody FindBoardSaveReqDto findBoardSaveReqDto) {
        findBoardService.findBoardCreate(findBoardSaveReqDto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "등록 완료","등록 완료");
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    // 게시글 상세 조회
    @GetMapping("/findboard/{id}")
    public ResponseEntity<CommonResDto> getFindBoard(@PathVariable Long id) {
        FindBoardResDto findBoardResDto = findBoardService.getResDto(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 성공", findBoardResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    // 게시글 목록 조회 (페이징 처리)
    @GetMapping("/findboard/list")
    public ResponseEntity<CommonResDto> getFindBoardList(Pageable pageable) {
        Page<FindBoardListResDto> findBoardListResDtos = findBoardService.findBoardListResDto(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 성공", findBoardListResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
}






















//    @PostMapping("/participate/{id}")
//    public ResponseEntity<CommonResDto> participate(@PathVariable Long id) {
//        FindBoardListResDto responseDto = findBoardService.participateInBoard(id);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "참여 성공", responseDto);
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }
//
//    @GetMapping("/list")
//    public ResponseEntity<CommonResDto> getFindBoardList() {
//        List<FindBoardListResDto> responseDtoList = findBoardService.getFindBoardList();
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "리스트 조회 성공", responseDtoList);
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }
//
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CommonResDto> getFindBoard(@PathVariable Long id) {
//        FindBoardResDto responseDto = findBoardService.getFindBoard(id);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 성공", responseDto);
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<CommonResDto> update(@PathVariable Long id, @RequestBody FindBoardUpdateReqDto findBoardUpdateReqDto) {
//        Long updatedId = findBoardService.update(id, findBoardUpdateReqDto);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "업데이트 성공", updatedId);
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }
//
//    @GetMapping("/detail/{id}")
//    public ResponseEntity<CommonResDto> getFindBoardDetail(@PathVariable Long id) {
//        FindBoardDetailResDto responseDto = findBoardService.getFindBoardDetail(id);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "상세 조회 성공", responseDto);
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }


//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<CommonResDto> delete(@PathVariable Long id) {
//        findBoardService.delete(id);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.NO_CONTENT, "게시글 삭제 성공", null);
//        return new ResponseEntity<>(commonResDto, HttpStatus.NO_CONTENT);
//    }
