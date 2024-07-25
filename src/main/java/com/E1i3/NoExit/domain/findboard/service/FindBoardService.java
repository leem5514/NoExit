package com.E1i3.NoExit.domain.findboard.service;

import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import com.E1i3.NoExit.domain.findboard.dto.*;
import com.E1i3.NoExit.domain.findboard.repository.FindBoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class FindBoardService {

    private final FindBoardRepository findBoardRepository;

    public FindBoardService(FindBoardRepository findBoardRepository) {
        this.findBoardRepository = findBoardRepository;
    }

    public FindBoardListResDto participateInBoard(Long id) {
        Optional<FindBoard> findBoardOptional = findBoardRepository.findById(id);
        if (findBoardOptional.isPresent()) {
            FindBoard findBoard = findBoardOptional.get();
            findBoard.incrementCurrentCount();
            findBoardRepository.save(findBoard);
            return FindBoardListResDto.fromEntity(findBoard);
        } else {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
    }


    public Long findBoardCreate(FindBoardSaveReqDto findBoardSaveReqDto) {
        FindBoard findBoard = findBoardSaveReqDto.toEntity();
        findBoardRepository.save(findBoard);
        return findBoard.getId();
    }

    public FindBoardResDto getFindBoard(Long id) {
        Optional<FindBoard> findBoardOptional = findBoardRepository.findById(id);
        if (findBoardOptional.isPresent()) {
            return FindBoardResDto.fromEntity(findBoardOptional.get());
        } else {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
    }

    public Long update(Long id, FindBoardUpdateReqDto findBoardUpdateReqDto) {
        Optional<FindBoard> findBoardOptional = findBoardRepository.findById(id);
        if (findBoardOptional.isPresent()) {
            FindBoard existingBoard = findBoardOptional.get();
            FindBoard updatedBoard = findBoardUpdateReqDto.toEntity(id, existingBoard);
            findBoardRepository.save(updatedBoard);
            return updatedBoard.getId();
        } else {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
    }

    public FindBoardDetailResDto getFindBoardDetail(Long id) {
        Optional<FindBoard> findBoardOptional = findBoardRepository.findById(id);
        if (findBoardOptional.isPresent()) {
            return FindBoardDetailResDto.fromEntity(findBoardOptional.get());
        } else {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
    }

    public void delete(Long id) {
        if (findBoardRepository.existsById(id)) {
            findBoardRepository.deleteById(id);
        } else {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
    }


    public List<FindBoardListResDto> getFindBoardList() {
        List<FindBoard> findBoardList = findBoardRepository.findAll();
        return findBoardList.stream()
                .map(FindBoardListResDto::fromEntity)
                .collect(Collectors.toList());
    }



}
