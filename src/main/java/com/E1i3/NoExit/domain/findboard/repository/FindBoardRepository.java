package com.E1i3.NoExit.domain.findboard.repository;

import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FindBoardRepository extends JpaRepository<FindBoard, Long> {



}
