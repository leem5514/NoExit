package com.E1i3.NoExit.domain.findboard.repository;

import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FindBoardRepository extends JpaRepository<FindBoard, Long> {

//  검색 커스텀
    Page<FindBoard> findAll(Specification<FindBoard> specification, Pageable pageable);
//    Page<FindBoard> findAll(Pageable pageable);
    Page<FindBoard> findByDelYn(Pageable pageable, DelYN delYn);
    Optional<FindBoard> findByIdAndDelYn(Long id, DelYN delYn);

    @Query("SELECT f FROM FindBoard f WHERE f.delYn = 'N' AND f.totalCapacity - f.currentCount = 1 ORDER BY f.expirationTime ASC")
    List<FindBoard> findImminentClosingBoards();
}
