package com.E1i3.NoExit.domain.findboard.service;

import com.E1i3.NoExit.domain.findboard.domain.DelYn;
import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardListResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardSaveReqDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardUpdateReqDto;
import com.E1i3.NoExit.domain.findboard.repository.FindBoardRepository;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    // 나중에 스프링시큐리티 토큰을 통한 인증 처리를 추가해서 새롭게 작성해야할 것 같다. 현재는 테스트를 위해 임시작성 7.27 , 김민성
    public void findBoardCreate(FindBoardSaveReqDto findBoardSaveReqDto) {
        // 모든 사용자 중 하나를 랜덤으로 선택
        List<Member> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new EntityNotFoundException("등록된 사용자가 없습니다.");
        }

        Member member = members.get(new Random().nextInt(members.size())); // 랜덤으로 사용자 선택

        FindBoard findBoard = findBoardSaveReqDto.toEntity(member);
        findBoardRepository.save(findBoard);
    }


    public FindBoardResDto getResDto(Long id) {
        FindBoard findBoard = findBoardRepository.findByIdAndDelYn(id, DelYn.Y)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않거나 삭제된 게시글입니다."));
        return findBoard.ResDtoFromEntity();
    }

    @Transactional(readOnly = true)
    public Page<FindBoardListResDto> findBoardListResDto(Pageable pageable) {
        // DelYn 필터 추가
        Page<FindBoard> findBoards = findBoardRepository.findByDelYn(pageable, DelYn.Y);
        return findBoards.map(FindBoard::listFromEntity);
    }

//    @Transactional(readOnly = true)
//    public Page<FindBoardListResDto> findBoardListResDto(int page) {
//        //페이징 처리 5
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdTime"));
//        return findBoardRepository.findAll(pageable)
//                .map(FindBoard::listFromEntity);
//    }


    @Transactional
    public FindBoardResDto update(Long id, FindBoardUpdateReqDto dto) {
        FindBoard findBoard = findBoardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        // 삭제된 게시글인지 체크
        if (findBoard.getDelYn() == DelYn.N) {
            throw new IllegalStateException("삭제된 게시글은 수정할 수 없습니다.");
        }

        findBoard.updateFromDto(dto);
        return findBoard.ResDtoFromEntity();
    }


    public String delete(Long id) {
        FindBoard findBoard = findBoardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        findBoard.markAsDeleted();
        return "게시글이 삭제되었습니다.";
    }

    public FindBoardResDto incrementParticipantCount(Long id) {
        FindBoard findBoard = findBoardRepository.findByIdAndDelYn(id, DelYn.Y)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        findBoard.incrementCurrentCount();
        return findBoard.ResDtoFromEntity();
    }
}
