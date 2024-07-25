package com.E1i3.NoExit.domain.findboard.controller;

import com.E1i3.NoExit.domain.findboard.dto.*;
import com.E1i3.NoExit.domain.findboard.service.FindBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/findboard")
public class FindBoardController {

    private final FindBoardService findBoardService;

    @Autowired
    public FindBoardController(FindBoardService findBoardService) {
        this.findBoardService = findBoardService;
    }

    @PostMapping("/participate/{id}")
    public FindBoardListResDto participate(@PathVariable Long id) {
        return findBoardService.participateInBoard(id);
    }

    @GetMapping("/list")
    public List<FindBoardListResDto> getFindBoardList() {
        return findBoardService.getFindBoardList();
    }

    //테스트 통과 7-24
    @PostMapping("/create")
    public Long findBoardCreate(@RequestBody FindBoardSaveReqDto findBoardSaveReqDto) {
        return findBoardService.findBoardCreate(findBoardSaveReqDto);
    }

    //테스트 통과 7-24
    @GetMapping("/{id}")
    public FindBoardResDto getFindBoard(@PathVariable Long id) {
        return findBoardService.getFindBoard(id);
    }

    //테스트 통과 7-24
    @GetMapping("/detail/{id}")
    public FindBoardDetailResDto getFindBoardDetail(@PathVariable Long id) {
        return findBoardService.getFindBoardDetail(id);
    }

    //테스트 통과 7-24
    @PutMapping("/update/{id}")
    public Long findBoardUpdate(@PathVariable Long id, @RequestBody FindBoardUpdateReqDto findBoardUpdateReqDto) {
        return findBoardService.update(id, findBoardUpdateReqDto);
    }

    //테스트 통과 7-24
    @DeleteMapping("/delete/{id}")
    public void findBoardDelete(@PathVariable Long id) {
        findBoardService.delete(id);
    }
}
