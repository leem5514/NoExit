package com.E1i3.NoExit.domain.attendance.controller;

import com.E1i3.NoExit.domain.attendance.dto.AttendanceResDto;
import com.E1i3.NoExit.domain.attendance.service.AttendanceService;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AttendanceController {

    private final AttendanceService attendanceService;
    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/attendance/list")
    public ResponseEntity<?> attendanceList() {
        List<AttendanceResDto> attendances = attendanceService.attendanceList();
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "OK", attendances), HttpStatus.OK);
    }

    // 새로운 기능 추가
    @GetMapping("/attendance/list/findBoard/{findBoardId}")
    public ResponseEntity<?> attendanceListByFindBoard(@PathVariable Long findBoardId) {
        List<AttendanceResDto> attendances = attendanceService.attendanceListByFindBoard(findBoardId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "OK", attendances), HttpStatus.OK);
    }


}

