package com.E1i3.NoExit.domain.findboard.controller;

import com.E1i3.NoExit.domain.common.dto.CommonResDto;
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

@RestController
public class FindBoardController {

    private final FindBoardService findBoardService;

    @Autowired
    public FindBoardController(FindBoardService findBoardService) {
        this.findBoardService = findBoardService;
    }

    @PostMapping("/findboard/create")
    public ResponseEntity<CommonResDto> findBoardCreate(@RequestBody FindBoardSaveReqDto findBoardSaveReqDto) {
        findBoardService.findBoardCreate(findBoardSaveReqDto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "게시글이 등록되었습니다.", "게시글 등록 완료");
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @GetMapping("/findboard/{id}")
    public ResponseEntity<CommonResDto> getFindBoard(@PathVariable Long id) {
        FindBoardResDto findBoardResDto = findBoardService.getResDto(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "게시글 조회 성공", findBoardResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/findboard/list")
    public ResponseEntity<CommonResDto> getFindBoardList(Pageable pageable) {
        Page<FindBoardListResDto> findBoardListResDtos = findBoardService.findBoardListResDto(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "게시글 목록 조회 성공", findBoardListResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PutMapping("/findboard/update/{id}")
    public ResponseEntity<CommonResDto> updateFindBoard(@PathVariable Long id, @RequestBody FindBoardUpdateReqDto findBoardUpdateReqDto) {
        FindBoardResDto updatedFindBoardResDto = findBoardService.update(id, findBoardUpdateReqDto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "게시글 업데이트 성공", updatedFindBoardResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PutMapping("/findboard/delete/{id}")
    public ResponseEntity<CommonResDto> deleteFindBoard(@PathVariable Long id) {
        String resultMessage = findBoardService.delete(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, resultMessage, "FindBoard 게시글 ID : "+id+" 삭제 완료");
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PutMapping("/findboard/increment/{id}")
    public ResponseEntity<CommonResDto> incrementParticipantCount(@PathVariable Long id) {
        FindBoardResDto updatedFindBoardResDto = findBoardService.incrementParticipantCount(id);

        if (updatedFindBoardResDto.getCurrentCount() >= updatedFindBoardResDto.getTotalCapacity()) {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.BAD_REQUEST, "참가자가 이미 가득 찼습니다.", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.BAD_REQUEST);
        }

        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "참가자 수 증가 성공", updatedFindBoardResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
}
