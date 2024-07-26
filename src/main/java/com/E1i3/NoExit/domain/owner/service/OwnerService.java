package com.E1i3.NoExit.domain.owner.service;

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
	private final MailVerifyService mailVerifyService;
	private final RedisService redisService;

	@Autowired
	private final PasswordEncoder passwordEncoder;

	private static final String AUTH_CODE_PREFIX = "AUTH_CODE ";

	@Value("${spring.mail.auth-code-expiration-millis}")
	private long authCodeExpirationMillis;

	public OwnerService(OwnerRepository ownerRepository, MailVerifyService mailVerifyService,
		RedisService redisService, PasswordEncoder passwordEncoder) {
		this.ownerRepository = ownerRepository;
		this.mailVerifyService = mailVerifyService;
		this.redisService = redisService;
		this.passwordEncoder = passwordEncoder;
	}

	// 회원 등록
	@Transactional
	public Owner owernerCreate(OwnerSaveReqDto ownerSaveReqDto) {
		Owner owner = ownerRepository.findByEmail(ownerSaveReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
		String encodedPassword = passwordEncoder.encode(ownerSaveReqDto.getPassword());
		return owner.saveOwner(ownerSaveReqDto, encodedPassword);
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
