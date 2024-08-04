package com.E1i3.NoExit.domain.attendance.repositroy;

import com.E1i3.NoExit.domain.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

}
