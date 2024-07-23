package com.E1i3.NoExit.domain.findboard.controller;

import com.E1i3.NoExit.domain.findboard.service.FindBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/findboard")
//@Api(tags = "Escape with me 서비스")
public class FindBoardController {
    private final FindBoardService findBoardService;

    @Autowired
    public FindBoardController(FindBoardService findBoardService) {
        this.findBoardService = findBoardService;
    }



}
