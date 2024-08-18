package com.E1i3.NoExit.domain.attendance.service;


import com.E1i3.NoExit.domain.attendance.domain.Attendance;
import com.E1i3.NoExit.domain.attendance.dto.AttendanceResDto;
import com.E1i3.NoExit.domain.attendance.repositroy.AttendanceRepository;
import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import com.E1i3.NoExit.domain.findboard.repository.FindBoardRepository;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final FindBoardRepository findBoardRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, MemberRepository memberRepository, FindBoardRepository findBoardRepository) {
        this.attendanceRepository = attendanceRepository;
        this.memberRepository = memberRepository;
        this.findBoardRepository = findBoardRepository;
    }

    @Transactional(readOnly = true)
    public List<AttendanceResDto> attendanceList() {
        List<Attendance> attendanceList = attendanceRepository.findAll();
        List<AttendanceResDto> attendanceResDtos = new ArrayList<>();

        for (Attendance attendance : attendanceList) {
            attendanceResDtos.add(attendance.listfromEntity());
        }

        return attendanceResDtos;
    }

}
