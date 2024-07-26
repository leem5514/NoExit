package com.E1i3.NoExit.domain.findboard.service;

import com.E1i3.NoExit.domain.findboard.domain.DelYn;
import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import com.E1i3.NoExit.domain.findboard.dto.*;
import com.E1i3.NoExit.domain.findboard.repository.FindBoardRepository;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
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
    @Transactional
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

    //이게 완성된 코드고 위에는 테스트를 위한 임시 코드
//    public void findBoardCreate(FindBoardSaveReqDto findBoardSaveReqDto) {
//        String nickname = "test"; // 하드코딩된 닉네임
//
//        Member member = memberRepository.findBynickname(nickname)
//                .orElseThrow(() -> new EntityNotFoundException("닉네임이 없습니다."));
//
//        FindBoard findBoard = findBoardSaveReqDto.toEntity(member);
//        findBoardRepository.save(findBoard);
//    }

    public FindBoardResDto getResDto(Long id) {
        FindBoard findBoard = findBoardRepository.findByIdAndDelYn(id, DelYn.Y)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않거나 삭제된 게시글입니다."));
        return findBoard.ResDtoFromEntity();
    }


    public Page<FindBoardListResDto> findBoardListResDto(Pageable pageable) {
        Page<FindBoard> findBoards = findBoardRepository.findByDelYn(pageable, DelYn.Y);
        return findBoards.map(FindBoard::listFromEintity);
    }


    @Transactional
    public String delete(Long id) {
        FindBoard findBoard = findBoardRepository.findByIdAndDelYn(id, DelYn.Y)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        findBoard.markAsDeleted();
        findBoardRepository.save(findBoard); // 상태를 업데이트하기 위해 저장
        return "삭제 완료";
    }


    @Transactional
    public FindBoardResDto update(Long id, FindBoardUpdateReqDto dto) {
        FindBoard findBoard = findBoardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        findBoard.updateFromDto(dto);
        return findBoard.ResDtoFromEntity();
    }

}
