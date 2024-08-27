package com.E1i3.NoExit.domain.attendance.repositroy;

import com.E1i3.NoExit.domain.attendance.domain.Attendance;
import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import com.E1i3.NoExit.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAll();
  	List<Attendance> findByMember(Member member);
    // 특정 FindBoard ID로 필터링된 Attendance 목록을 조회하는 메서드
    List<Attendance> findByFindBoardId(Long findBoardId);

	List<Attendance> findByEmailAndChatRoomIsNotNull(String email);

    // 특정 게시글 ID에 해당하는 모든 Attendance 엔티티의 delYn 값을 Y로 업데이트하는 메서드
    @Modifying
    @Query(value = "UPDATE attendance SET del_yn = 'Y' WHERE findboard_id = :findBoardId", nativeQuery = true)
    void markAttendancesAsDeletedByFindBoardId(@Param("findBoardId") Long findBoardId);

}
