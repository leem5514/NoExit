package com.E1i3.NoExit.domain.member.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.dto.MailReqDto;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;

	private final MailService mailService;

	private final RedisService redisService;

	private static final String AUTH_CODE_PREFIX = "AUTH_CODE ";

	public MemberService(MemberRepository memberRepository, MailService mailService, RedisService redisService) {
		this.memberRepository = memberRepository;
		this.mailService = mailService;
		this.redisService = redisService;
	}

	@Value("${spring.mail.auth-code-expiration-millis}")
	private long authCodeExpirationMillis;

	// 	인증번호 생성
	private String createCode() {
		StringBuilder sb = new StringBuilder();	//인증번호
		try{
			Random random = SecureRandom.getInstanceStrong();
			for (int i = 0; i < 6; i++) {
				// 6자리 랜덤 수 생성 -> 추후에 문자와 숫자 섞는걸로 수정
				sb.append(random.nextInt(10));
			}
		}catch (NoSuchAlgorithmException e) {
			// common - error코드로 수정필요
			e.printStackTrace();
		}
		return sb.toString();
	}

	// 	인증번호 검증
	// 이메일을 입력받아 redis의 인증코드와 파라미터의 인증코드가 동일한지 비교
	// 일치하면 true, 불일치하면 false;
	public boolean verifiedCode(String email, String authCode) {
		// 	존재하는 회원 정보가 있는지 확인
		boolean result = false;
		chkDuplicatedEmail(email);
		String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
		if(redisService.checkExistsValue(redisAuthCode)){
			result =  redisAuthCode.equals(authCode);
		}
		return result;
	}

	public void sendCodeToEmail(String email) {
		chkDuplicatedEmail(email);

		String title = "이메일 인증 번호";
		String authCode = this.createCode();
		MailReqDto mailReqDto = MailReqDto.builder()
			.receiver(email)
			.title(title)
			.contents(authCode)
			.build();
		// 메일 전송하고
		mailService.sendEmail(mailReqDto);
		// redis에 저장
		redisService.setValues(AUTH_CODE_PREFIX + email, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
	}

	public void chkDuplicatedEmail(String email) {
		Optional<Member> member = memberRepository.findByEmail(email);
		if(member.isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 회원 정보입니다.");
		}
	}
}
