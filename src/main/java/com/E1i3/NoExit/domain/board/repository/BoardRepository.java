package com.E1i3.NoExit.domain.board.repository;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAll(Specification<Board> specification, Pageable pageable);
    Page<Board> findByDelYN(Pageable pageable, DelYN delYN);
}