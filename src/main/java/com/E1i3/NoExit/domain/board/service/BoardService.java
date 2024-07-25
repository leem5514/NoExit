package com.E1i3.NoExit.domain.board.service;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.dto.BoardCreateReqDto;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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






    public Page<BoardListResDto> boardList(Pageable pageable){ // 게시글 전체 조회
        Page<Board> boards = boardRepository.findAll(pageable);
        Page<BoardListResDto> boardListResDtos = boards.map(
                a->a.fromEntity());

        return boardListResDtos;
    }





    public BoardDetailResDto boardDetail(Long id) { // 특정 게시글 조회
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
        return board.detailFromEntity();
    }





    @Transactional
    public Board boardUpdate(Long id, BoardUpdateReqDto dto) {
        Board board = boardRepository.findById(id).orElseThrow(()->new EntityNotFoundException(" 찾을 수 없습니다."));
        board.updateEntity(dto);
        Board updatedBoard = boardRepository.save(board);
        return updatedBoard;
    }






    public void boardDelete(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(()->new EntityNotFoundException("찾을 수 없습니다."));
        boardRepository.delete(board);
    }
}

