package com.E1i3.NoExit.domain.owner.service;

import java.time.Duration;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.E1i3.NoExit.domain.common.dto.LoginReqDto;
import com.E1i3.NoExit.domain.common.service.RedisService;
import com.E1i3.NoExit.domain.mail.service.MailVerifyService;
import com.E1i3.NoExit.domain.owner.domain.Owner;
import com.E1i3.NoExit.domain.owner.dto.OwnerListResDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerSaveReqDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerUpdateDto;
import com.E1i3.NoExit.domain.owner.repository.OwnerRepository;

@Service
public class OwnerService {

	private final OwnerRepository ownerRepository;
	private final RedisService redisService;
	private final PasswordEncoder passwordEncoder;

	private static final String AUTH_EMAIL_PREFIX = "EMAIL_CERTIFICATE ";

	@Autowired
	public OwnerService(OwnerRepository ownerRepository,
		RedisService redisService, PasswordEncoder passwordEncoder) {
		this.ownerRepository = ownerRepository;
		this.redisService = redisService;
		this.passwordEncoder = passwordEncoder;
	}

	// 회원 등록
	// @Transactional
	// public Owner ownerCreate(OwnerSaveReqDto ownerSaveReqDto) {
	// 	Owner owner = ownerRepository.findByEmail(ownerSaveReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
	// 	String encodedPassword = passwordEncoder.encode(ownerSaveReqDto.getPassword());
	// 	return owner.saveOwner(ownerSaveReqDto, encodedPassword);
	// }

	@Transactional
	public Owner ownerCreate(OwnerSaveReqDto ownerSaveReqDto) {
		// 레디스에 인증이 된 상태인지 확인
		String chkVerified = redisService.getValues(AUTH_EMAIL_PREFIX + ownerSaveReqDto.getEmail());
		if (chkVerified == null || !chkVerified.equals("true")) {
			throw new IllegalStateException("이메일 인증이 필요합니다.");
		}

		// 이메일로 회원을 검색
		ownerRepository.findByEmail(ownerSaveReqDto.getEmail()).ifPresent(existingMember -> {
			throw new EntityExistsException("이미 존재하는 이메일입니다.");
		});
		String encodedPassword = passwordEncoder.encode(ownerSaveReqDto.getPassword());
		return ownerRepository.save(ownerSaveReqDto.toEntity(encodedPassword));
	}

	// 회원 조회
	public Page<OwnerListResDto> ownerList(Pageable pageable) {
		Page<Owner> memberList = ownerRepository.findAll(pageable);
		return memberList.map(a -> a.fromEntity());
	}

	// 회원 삭제
	@Transactional
	public Owner ownerDelete(String email) {
		Owner owner = ownerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
		return owner.updateDelYN();
	}

	// 회원 정보 수정
	@Transactional
	public Owner memberUpdate(OwnerUpdateDto ownerUpdateDto) {
		Owner member = ownerRepository.findByEmail(ownerUpdateDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
		String encodedPassword = passwordEncoder.encode(ownerUpdateDto.getPassword());
		return member.updateOwner(ownerUpdateDto, encodedPassword);
	}

	// 로그인
	public Owner login(LoginReqDto loginReqDto) {
		// 	email의 존재여부
		Owner owner = ownerRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		// 	password 일치 여부
		if(!passwordEncoder.matches(loginReqDto.getPassword(), owner.getPassword())){
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		return owner;
	}
}
