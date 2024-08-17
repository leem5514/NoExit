package com.E1i3.NoExit.domain.attendance.service;

import com.E1i3.NoExit.domain.attendance.domain.Attendance;
import com.E1i3.NoExit.domain.attendance.dto.AttendanceResDto;
import com.E1i3.NoExit.domain.attendance.repositroy.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<AttendanceResDto> attendanceList(){

        List<Attendance> attendanceList = attendanceRepository.findAll();
        List<AttendanceResDto> attendanceResDtos = new ArrayList<>();

        for (Attendance attendance : attendanceList){
            attendanceResDtos.add(attendance.listfromEntity());
        }

        return attendanceResDtos;
    }

}
