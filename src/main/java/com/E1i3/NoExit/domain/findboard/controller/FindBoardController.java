package com.E1i3.NoExit.domain.findboard.controller;

import com.E1i3.NoExit.domain.common.CommonResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardListResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardSaveReqDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardUpdateReqDto;
import com.E1i3.NoExit.domain.findboard.service.FindBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

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

    @GetMapping("/findboard/{id}")
    public ResponseEntity<CommonResDto> getFindBoard(@PathVariable Long id) {

        try {
            FindBoardResDto findBoardResDto = findBoardService.getResDto(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 성공", findBoardResDto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            // 게시글이 존재하지 않거나 삭제된 상태인 경우
            CommonResDto commonResDto = new CommonResDto(HttpStatus.NOT_FOUND, "게시글이 존재하지 않거나 삭제된 게시글입니다.", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/findboard/list")
    public ResponseEntity<CommonResDto> getFindBoardList(Pageable pageable) {
        Page<FindBoardListResDto> findBoardListResDtos = findBoardService.findBoardListResDto(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 성공", findBoardListResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    @PutMapping("/findboard/update/{id}")
    public ResponseEntity<CommonResDto> updateFindBoard(@PathVariable Long id, @RequestBody FindBoardUpdateReqDto findBoardUpdateReqDto) {
        FindBoardResDto updatedFindBoardResDto = findBoardService.update(id, findBoardUpdateReqDto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "업데이트 성공", updatedFindBoardResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    // delete 맵핑하지않고 put맵핑으로 DelYn만 N으로 변경하기
    @PutMapping("/findboard/delete/{id}")
    public ResponseEntity<CommonResDto> deleteFindBoard(@PathVariable Long id) {
        String resultMessage = findBoardService.delete(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, resultMessage, "삭제 완료");

        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


}
