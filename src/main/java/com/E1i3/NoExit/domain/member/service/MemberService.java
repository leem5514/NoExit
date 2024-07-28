package com.E1i3.NoExit.domain.member.service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.E1i3.NoExit.domain.common.dto.LoginReqDto;
import com.E1i3.NoExit.domain.common.service.RedisService;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.member.dto.MemberListResDto;
import com.E1i3.NoExit.domain.member.dto.MemberSaveReqDto;
import com.E1i3.NoExit.domain.member.dto.MemberUpdateDto;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.owner.controller.domain.Owner;
import com.E1i3.NoExit.domain.owner.repository.OwnerRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final RedisService redisService;
	private final PasswordEncoder passwordEncoder;

	private static final String AUTH_EMAIL_PREFIX = "EMAIL_CERTIFICATE ";
	private final OwnerRepository ownerRepository;

	@Autowired
	public MemberService(MemberRepository memberRepository, RedisService redisService, PasswordEncoder passwordEncoder,
		OwnerRepository ownerRepository) {
		this.memberRepository = memberRepository;
		this.redisService = redisService;
		this.passwordEncoder = passwordEncoder;
		this.ownerRepository = ownerRepository;
	}

	// 회원 등록
	@Transactional
	public Member memberCreate(MemberSaveReqDto memberSaveReqDto) {
		// 레디스에 인증이 된 상태인지 확인
		String chkVerified = redisService.getValues(AUTH_EMAIL_PREFIX + memberSaveReqDto.getEmail());
		if (chkVerified == null || !chkVerified.equals("true")) {
			throw new IllegalStateException("이메일 인증이 필요합니다.");
		}

		// 이메일로 회원을 검색
		memberRepository.findByEmail(memberSaveReqDto.getEmail()).ifPresent(existingMember -> {
			throw new EntityExistsException("이미 존재하는 이메일입니다.");
		});
		String encodedPassword = passwordEncoder.encode(memberSaveReqDto.getPassword());
		return memberRepository.save(memberSaveReqDto.toEntity(encodedPassword));
	}

	// 회원 조회
	public Page<MemberListResDto> memberList(Pageable pageable) {
		Page<Member> memberList = memberRepository.findAll(pageable);
		return memberList.map(a -> a.fromEntity());
	}

	// 회원 삭제
	@Transactional
	public Member memberDelete(String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
		return member.updateDelYN();
	}

	// 회원 정보 수정
	@Transactional
	public Member memberUpdate(MemberUpdateDto memberUpdateDto) {
		Member member = memberRepository.findByEmail(memberUpdateDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
		String encodedPassword = passwordEncoder.encode(memberUpdateDto.getPassword());
		return member.updateMember(memberUpdateDto, encodedPassword);
	}

	// 로그인
	public Object login(LoginReqDto loginReqDto) {
		if (loginReqDto.getRole().equals(Role.USER)) {
			Member member = memberRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
			// 	password 일치 여부
			if(!passwordEncoder.matches(loginReqDto.getPassword(), member.getPassword())){
				throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
			}
			return member;
		}else if(loginReqDto.getRole().equals(Role.OWNER)) {
			Owner owner = ownerRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
			// 	password 일치 여부
			if(!passwordEncoder.matches(loginReqDto.getPassword(), owner.getPassword())){
				throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
			}
			return owner;
		}
		return null;
	}


}
