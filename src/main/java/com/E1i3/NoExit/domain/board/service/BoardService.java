package com.E1i3.NoExit.domain.board.service;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.dto.BoardCreateReqDto;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    @Transactional
    public void boardCreate(BoardCreateReqDto dto) { // 게시글 생성
        Board board = dto.toEntity();
        boardRepository.save(board);
    }





    public List<BoardListResDto> boardList() { // 게시글 전체 조회
        List<BoardListResDto> boardListResDtos = new ArrayList<>();
        for(Board b : boardRepository.findAll()) {
            boardListResDtos.add(b.fromEntity());
        }
        return boardListResDtos;
    }





    public BoardDetailResDto boardDetail(Long id) { // 특정 게시글 조회
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
        return board.detailFromEntity();
    }


    public void boardUpdate(BoardUpdateReqDto dto) {

    }

    public void boardDelete(Long id) {
    }


}

