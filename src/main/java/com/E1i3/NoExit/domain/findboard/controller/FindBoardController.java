package com.E1i3.NoExit.domain.findboard.controller;

import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardListResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardSaveReqDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardUpdateReqDto;
import com.E1i3.NoExit.domain.findboard.service.FindBoardService;
import com.E1i3.NoExit.domain.member.dto.MemberUpdateDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/findboard")
@Api(tags="Escape with me 서비스")
public class FindBoardController {

    private final FindBoardService findBoardService;

    @Autowired
    public FindBoardController(FindBoardService findBoardService) {
        this.findBoardService = findBoardService;
    }

    // 나중에 스프링시큐리티 토큰을 통한 인증 처리를 추가해서 새롭게 작성해야할 것 같다.
    @PostMapping("/create")
    public ResponseEntity<Object> findBoardCreate(@RequestBody FindBoardSaveReqDto findBoardSaveReqDto) {
        findBoardService.findBoardCreate(findBoardSaveReqDto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "등록 완료","등록 완료");
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResDto> getFindBoard(@PathVariable Long id) {
        FindBoardResDto findBoardResDto = findBoardService.getResDto(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "게시글 조회 성공", findBoardResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResDto> getFindBoardList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdTime") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        // 페이지 번호를 1에서 0으로 조정
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(direction), sort));
        Page<FindBoardListResDto> findBoardListResDtos = findBoardService.findBoardListResDto(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "조회 성공", findBoardListResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResDto> updateFindBoard(@PathVariable Long id, @RequestBody FindBoardUpdateReqDto findBoardUpdateReqDto) {
        FindBoardResDto updatedFindBoardResDto = findBoardService.update(id, findBoardUpdateReqDto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "업데이트 성공", updatedFindBoardResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<CommonResDto> deleteFindBoard(@PathVariable Long id) {
        String resultMessage = findBoardService.delete(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, resultMessage, "삭제 완료");
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PutMapping("/participate/{id}")
    public ResponseEntity<CommonResDto> incrementParticipantCount(@PathVariable Long id) {
        try {
            FindBoardResDto updatedFindBoardResDto = findBoardService.incrementParticipantCount(id);
            if (updatedFindBoardResDto.getCurrentCount() >= updatedFindBoardResDto.getTotalCapacity()) {
                return new ResponseEntity<>(new CommonResDto(HttpStatus.BAD_REQUEST, "참가자가 이미 가득 찼습니다.", null), HttpStatus.BAD_REQUEST);
            }
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "참가자 수 증가 성공", updatedFindBoardResDto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.NOT_FOUND, "게시글이 존재하지 않거나 삭제된 게시글입니다.", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.NOT_FOUND);
        }
    }
}
