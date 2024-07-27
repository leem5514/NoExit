package com.E1i3.NoExit.domain.owner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.E1i3.NoExit.domain.common.auth.JwtTokenProvider;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.mail.service.MailVerifyService;
import com.E1i3.NoExit.domain.owner.dto.OwnerSaveReqDto;
import com.E1i3.NoExit.domain.owner.service.OwnerService;

@RestController
public class OwnerController {

	@Autowired
	private final OwnerService OwnerService;
	@Autowired
	private MailVerifyService mailVerifyService;
	@Autowired
	private final JwtTokenProvider jwtTokenProvider;

	public OwnerController(OwnerService OwnerService, JwtTokenProvider jwtTokenProvider) {
		this.OwnerService = OwnerService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/owner/create")
	public ResponseEntity<CommonResDto> ownerCreate(@RequestBody OwnerSaveReqDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "회원가입 성공", OwnerService.ownerCreate(dto).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}
}
