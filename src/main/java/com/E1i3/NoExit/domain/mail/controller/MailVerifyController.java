package com.E1i3.NoExit.domain.mail.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.mail.dto.MailReqDto;
import com.E1i3.NoExit.domain.mail.service.MailVerifyService;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.dto.MemberSaveReqDto;
import com.E1i3.NoExit.domain.member.service.MemberService;

@RestController
@RequestMapping("/email")
public class MailVerifyController {

	private final MailVerifyService mailVerifyService;
	private final MemberService memberService;

	@Autowired
	public MailVerifyController(MailVerifyService mailVerifyService, MemberService memberService) {
		this.mailVerifyService = mailVerifyService;
		this.memberService = memberService;
	}

	@PostMapping("/requestCode")
	public ResponseEntity<CommonResDto> memberCreatePost(@RequestParam("email") @Valid String email) {
		// Member member = memberService.sendCodeToEmail(email);
		Member member = memberService.sendCodeToEmail(email);
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "이메일 인증 요청", member.getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// // 인증 이메일 요청
	// @PostMapping("/requestCode")
	// public ResponseEntity<?> reqeustCode(MailReqDto mailReqDto) {
	// 	// 회원정보 입력받고 그 이메일로 인증번호 전송 요청
	// 	mailVerifyService.sendEmail(mailReqDto);
	// 	return new ResponseEntity<>(HttpStatus.OK);
	// }


	// 인증번호 검증 요청
	@GetMapping("/requestCode")
	public ResponseEntity<CommonResDto> verificationEmail(@RequestParam("email") @Valid String email,
		@RequestParam("code") String authCode) {
		boolean response = mailVerifyService.verifiedCode(email, authCode);
		if (!response) {
			// 	일치하지 않는다면 MariaDB 삭제
			memberService.memberDelete(email);
		}
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "이메일 인증 성공", response);
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}
}
