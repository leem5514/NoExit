package com.E1i3.NoExit.domain.member.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.E1i3.NoExit.domain.mail.service.MailVerifyService;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;

import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.member.dto.MemberListResDto;
import com.E1i3.NoExit.domain.member.dto.MemberSaveReqDto;
import com.E1i3.NoExit.domain.member.dto.MemberUpdateDto;
import com.E1i3.NoExit.domain.member.service.MemberService;
import com.E1i3.NoExit.domain.owner.domain.Owner;
import com.E1i3.NoExit.domain.owner.service.OwnerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MemberController {

	@Autowired
	private final MemberService memberService;
	@Autowired
	private final OwnerService ownerService;
	@Autowired
	private MailVerifyService mailVerifyService;
	@Autowired
	private final JwtTokenProvider jwtTokenProvider;

	public MemberController(MemberService memberService, OwnerService ownerService, JwtTokenProvider jwtTokenProvider) {
		this.memberService = memberService;
		this.ownerService = ownerService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	// 회원가입 /member/create
	@PostMapping("/member/create")
	public ResponseEntity<CommonResDto> memberCreatePost(@RequestBody MemberSaveReqDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원가입 성공", memberService.memberCreate(dto).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}


	// 상세 내역 수정
	@PostMapping("/member/update")
	public ResponseEntity<CommonResDto> updateMember(@RequestBody MemberUpdateDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원정보 수정", memberService.memberUpdate(dto).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// 탈퇴 (?)
	@PostMapping("/member/delete")
	public ResponseEntity<CommonResDto> deleteMember(@RequestBody MemberUpdateDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원정보 삭제",  memberService.memberDelete(dto.getEmail()).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// 회원 리스트
	@GetMapping("/member/list")
	public ResponseEntity<Object> memberList(Pageable pageable) {
		Page<MemberListResDto> members = memberService.memberList(pageable);
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "member List", members.toList()), HttpStatus.OK);
	}

	// 회원 정보(마이페이지) 조회하는 api필요 -> 토큰으로 처리

	@PostMapping("/doLogin")
	public ResponseEntity<Object> doLogin(@RequestBody LoginReqDto loginReqDto) {
		// email, password가 일치하는지 검증
		Map<String, Object> loginInfo = new HashMap<>();
		Object user = memberService.login(loginReqDto);

		if (user instanceof Member) {
			Member member = (Member) user;
			String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
			loginInfo.put("id", member.getId());
			loginInfo.put("token", jwtToken);
			return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "Member login", loginInfo), HttpStatus.OK);
		} else if (user instanceof OwnerService) {
			Owner owner = (Owner) user;
			String jwtToken = jwtTokenProvider.createToken(owner.getEmail(), owner.getRole().toString());
			loginInfo.put("id", owner.getId());
			loginInfo.put("token", jwtToken);
			return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "OWNER login", loginInfo), HttpStatus.OK);
		}
		// 생성된 토큰을 comonResDto에 담아서 사용자에게 리턴
		return null;
	}
}
