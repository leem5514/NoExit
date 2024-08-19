package com.E1i3.NoExit.domain.attendance.repositroy;

import com.E1i3.NoExit.domain.attendance.domain.Attendance;
import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import com.E1i3.NoExit.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAll();
    List<Attendance> findByFindBoardId(Long id);

	  List<Attendance> findByMember(Member member);
    // 특정 FindBoard ID로 필터링된 Attendance 목록을 조회하는 메서드
    List<Attendance> findByFindBoardId(Long findBoardId);
}
