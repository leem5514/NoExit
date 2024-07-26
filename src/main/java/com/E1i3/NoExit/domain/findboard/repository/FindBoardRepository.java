package com.E1i3.NoExit.domain.findboard.repository;

import com.E1i3.NoExit.domain.findboard.domain.DelYn;
import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FindBoardRepository extends JpaRepository<FindBoard, Long> {

    Page<FindBoard> findAll(Pageable pageable);
    Page<FindBoard> findByDelYn(Pageable pageable, DelYn delYn);
}
