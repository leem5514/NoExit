package com.E1i3.NoExit.domain.boardimage.repository;

import com.E1i3.NoExit.domain.boardimage.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

}