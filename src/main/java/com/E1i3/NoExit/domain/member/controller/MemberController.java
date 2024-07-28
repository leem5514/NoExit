package com.E1i3.NoExit.domain.member.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;

import com.E1i3.NoExit.domain.member.dto.MemberSaveReqDto;
import com.E1i3.NoExit.domain.member.dto.MemberUpdateDto;
import com.E1i3.NoExit.domain.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/members")
public class MemberController {

	@Autowired
	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	// 회원가입 /members/emails/requestCode
	@PostMapping("/emails/requestCode")
	public ResponseEntity<CommonResDto> memberCreatePost(@RequestBody MemberSaveReqDto dto) {
		// 일단 mariadb에 저장
		// @RequestParam("email") @Valid String email
		// 회원정보 입력받고 그 이메일로 인증번호 전송 요청
		memberService.sendCodeToEmail(dto.getEmail());
		Member member = memberService.memberCreate(dto);
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "이메일 인증 요청", member);
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// 인증번호 검증 요청
	@GetMapping("/emails/requestCode")
	public ResponseEntity<CommonResDto> verificationEmail(@RequestParam("email") @Valid String email,
		@RequestParam("code") String authCode) {
		boolean response = memberService.verifiedCode(email, authCode);
		if (!response) {
			// 	일치하지 않는다면 mariadb에서 삭제
			memberService.memberDelete(email);
		}
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "이메일 인증 성공", "");
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// @PostMapping("/create")
	// @ResponseBody
	// public String createMember(@RequestBody MemberSaveReqDto dto) {
	// 	memberService.memberCreate(dto);
	// 	return "create ok";
	// }

	// 상세 내역 수정
	@PostMapping("/update")
	@ResponseBody
	public ResponseEntity<CommonResDto> updateMember(@RequestBody MemberUpdateDto dto) {
		Member member = memberService.memberUpdate(dto);
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원정보 수정", "");
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// 탈퇴 (?)
	@PostMapping("/delete")
	@ResponseBody
	public ResponseEntity<CommonResDto> deleteMember(@RequestBody MemberUpdateDto dto) {
		Member member = memberService.memberDelete(dto.getEmail());
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원정보 삭제", "");
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

}
