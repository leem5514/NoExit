package com.E1i3.NoExit.domain.member.controller;

import java.util.HashMap;
import java.util.Map;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.E1i3.NoExit.domain.common.auth.JwtTokenProvider;
import com.E1i3.NoExit.domain.common.dto.LoginReqDto;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.member.dto.MemberDetResDto;
import com.E1i3.NoExit.domain.member.dto.MemberListResDto;
import com.E1i3.NoExit.domain.member.dto.MemberSaveReqDto;
import com.E1i3.NoExit.domain.member.dto.MemberUpdateDto;
import com.E1i3.NoExit.domain.member.service.MemberService;
import com.E1i3.NoExit.domain.notification.service.NotificationService;
import com.E1i3.NoExit.domain.owner.domain.Owner;
import com.E1i3.NoExit.domain.owner.service.OwnerService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController

@Api(tags="회원 서비스")
public class MemberController {

	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;
	private final NotificationService notificationService;

	@Autowired
	public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider,
		NotificationService notificationService, NotificationService notificationService1) {
		this.memberService = memberService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.notificationService = notificationService1;
	}

	// 회원가입 /member/create
	@Operation(summary= "[일반 사용자] 회원가입 API")
	@PostMapping("/member/create")
	public ResponseEntity<CommonResDto> memberCreatePost(@RequestBody MemberSaveReqDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원가입 성공", memberService.memberCreate(dto).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}


	// 상세 내역 수정
	@Operation(summary= "[일반 사용자] 사용자 정보 수정 API")
	@PostMapping("/member/update")
	public ResponseEntity<CommonResDto> updateMember(@RequestBody MemberUpdateDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원정보 수정", memberService.memberUpdate(dto).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// 탈퇴 (?)
	@Operation(summary= "[일반 사용자] 사용자 탈퇴 API")
	@PostMapping("/member/delete")
	public ResponseEntity<CommonResDto> deleteMember(@RequestBody MemberUpdateDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원정보 삭제",  memberService.memberDelete(dto.getEmail()).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// 회원 정보(마이페이지)
	@Operation(summary= "[일반 사용자] 마이페이지 API")
	@GetMapping("/member/myInfo")
	public ResponseEntity<Object> myInfo() {
		MemberDetResDto member = memberService.myInfo();
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "member List", member), HttpStatus.OK);
	}

	@Operation(summary= "[사용자] 로그인 API")
	@PostMapping("/doLogin")
	public ResponseEntity<Object> doLogin(@RequestBody LoginReqDto loginReqDto) {
		Map<String, Object> loginInfo = new HashMap<>();
		Object user = memberService.login(loginReqDto);

		if (user instanceof Member) {
			Member member = (Member) user;
			String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
			notificationService.subscribe(Role.USER);
			loginInfo.put("id", member.getId());
			loginInfo.put("token", jwtToken);
			return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "일반 사용자 로그인 성공", loginInfo), HttpStatus.OK);
		} else if (user instanceof OwnerService) {
			Owner owner = (Owner) user;
			String jwtToken = jwtTokenProvider.createToken(owner.getEmail(), owner.getRole().toString());
			notificationService.subscribe(Role.OWNER);
			loginInfo.put("id", owner.getId());
			loginInfo.put("token", jwtToken);
			return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "사용자 로그인 성공", loginInfo), HttpStatus.OK);
		}
		// 생성된 토큰을 comonResDto에 담아서 사용자에게 리턴
		return null;
	}
}
