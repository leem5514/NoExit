package com.E1i3.NoExit.domain.findboard.service;

import com.E1i3.NoExit.domain.findboard.repository.FindBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FindBoardService {

    private final FindBoardRepository findBoardRepository;

    @Autowired
    public FindBoardService(FindBoardRepository findBoardRepository){
        this.findBoardRepository = findBoardRepository;
    }

}
