package com.E1i3.NoExit.domain.findboard.service;

import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import com.E1i3.NoExit.domain.findboard.dto.*;
import com.E1i3.NoExit.domain.findboard.repository.FindBoardRepository;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class FindBoardService {

    private final FindBoardRepository findBoardRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public FindBoardService(FindBoardRepository findBoardRepository, MemberRepository memberRepository) {

        this.findBoardRepository = findBoardRepository;
        this.memberRepository = memberRepository;
    }

    public void findBoardCreate(FindBoardSaveReqDto findBoardSaveReqDto) {

        //나중에 Security 붙여서 처리하는 로직을 추가해야한다.
        FindBoard findBoard = findBoardSaveReqDto.toEntity();
        findBoardRepository.save(findBoard);

    }

    public FindBoardResDto getResDto (Long id) {
        FindBoard findBoard = findBoardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        FindBoardResDto resDto = findBoard.ResDtoFromEntity();

        return resDto;
    }

    // 페이징 처리 + delYn 상태 Y만 찾아서 반환.
    public Page<FindBoardListResDto> findBoardListResDto(Pageable pageable){
        Page<FindBoard> findBoards = findBoardRepository.findByDelYn(pageable,"Y");
        Page<FindBoardListResDto> findBoardListResDtos = findBoards.map(a -> a.listFromEintity());
        return findBoardListResDtos;
    }














//    이거 보류
//    public FindBoardDetailResDto getDetailResDto (Long id) {
//
//        FindBoard findBoard = findBoardRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
//        FindBoardDetailResDto detailResDto = findBoard.detailResDtoFromEntity();
//
//        return detailResDto;
//    }

}
