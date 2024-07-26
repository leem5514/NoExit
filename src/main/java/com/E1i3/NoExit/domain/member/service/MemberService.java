package com.E1i3.NoExit.domain.member.service;

import java.time.Duration;

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
import com.E1i3.NoExit.domain.mail.dto.MailReqDto;
import com.E1i3.NoExit.domain.mail.service.MailVerifyService;
import com.E1i3.NoExit.domain.member.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.dto.MemberListResDto;
import com.E1i3.NoExit.domain.member.dto.MemberSaveReqDto;
import com.E1i3.NoExit.domain.member.dto.MemberUpdateDto;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final MailVerifyService mailVerifyService;
	private final RedisService redisService;

	@Autowired
	private final PasswordEncoder passwordEncoder;

	private static final String AUTH_CODE_PREFIX = "AUTH_CODE ";

	@Value("${spring.mail.auth-code-expiration-millis}")
	private long authCodeExpirationMillis;

	public MemberService(MemberRepository memberRepository, MailVerifyService mailVerifyService,
		RedisService redisService, PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.mailVerifyService = mailVerifyService;
		this.redisService = redisService;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public Member sendCodeToEmail(String email) {
		String title = "이메일 인증 번호";
		String authCode = mailVerifyService.createCode();
		MailReqDto mailReqDto = MailReqDto.builder()
			.receiver(email)
			.title(title)
			.contents(authCode)
			.build();

		// 메일 전송하고
		mailVerifyService.sendEmail(mailReqDto);
		// redis에 code 저장
		redisService.setValues(AUTH_CODE_PREFIX + email, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
		// 이메일만 존재하는 상태로 db에 저장
		Member member = Member.builder().username("").password("").phone_number("").nickname("").delYN(DelYN.N).email(email).build();
		return memberRepository.save(member);
	}


	// 회원 등록
	@Transactional
	public Member memberCreate(MemberSaveReqDto memberSaveReqDto) {
		Member member = memberRepository.findByEmail(memberSaveReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));
		String encodedPassword = passwordEncoder.encode(memberSaveReqDto.getPassword());
		return member.saveMember(memberSaveReqDto, encodedPassword);
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
	public Member login(LoginReqDto loginReqDto) {
		// 	email의 존재여부
		Member member = memberRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		// 	password 일치 여부
		if(!passwordEncoder.matches(loginReqDto.getPassword(), member.getPassword())){
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		return member;
	}


}
