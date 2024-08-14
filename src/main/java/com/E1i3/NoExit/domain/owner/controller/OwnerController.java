package com.E1i3.NoExit.domain.owner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.E1i3.NoExit.domain.common.auth.JwtTokenProvider;
import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.member.dto.MemberSaveReqDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerDetResDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerListResDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerUpdateDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerSaveReqDto;
import com.E1i3.NoExit.domain.owner.service.OwnerService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/owner")
// @Tag(name = "owner", description = "점주 API")
public class OwnerController {

	@Autowired
	private final OwnerService ownerService;
	@Autowired
	private final JwtTokenProvider jwtTokenProvider;

	public OwnerController(OwnerService ownerService, JwtTokenProvider jwtTokenProvider) {
		this.ownerService = ownerService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Operation(summary= "[점주 사용자] 회원가입 API")
	@PostMapping("/create")
	public ResponseEntity<CommonResDto> ownerCreate(@RequestPart(value = "data") OwnerSaveReqDto dto, @RequestPart(value = "file") MultipartFile imgFile) {
	// public ResponseEntity<CommonResDto> ownerCreate(@RequestBody OwnerSaveReqDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "점주 회원가입 성공", ownerService.ownerCreate(dto).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// 상세 내역 수정
	@Operation(summary= "[점주 사용자] 점주 정보 수정 API")
	@PostMapping("/update")
	public ResponseEntity<CommonResDto> updateOwner(@RequestBody OwnerUpdateDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "점주 회원정보 수정", ownerService.ownerUpdate(dto).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// 탈퇴 (?)
	@Operation(summary= "[점주 사용자] 점주 탈퇴 API")
	@PostMapping("/delete")
	public ResponseEntity<CommonResDto> deleteOwner(@RequestBody OwnerUpdateDto dto) {
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "점주 회원정보 삭제",  ownerService.ownerDelete(dto.getEmail()).getId());
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}

	// 회원 정보(마이페이지)
	@Operation(summary= "[점주 사용자] 마이페이지 API")
	@GetMapping("/myInfo")
	public ResponseEntity<Object> myInfo() {
		OwnerDetResDto owner = ownerService.myInfo();
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "점주 회원정보 조회", owner), HttpStatus.OK);
	}
}
