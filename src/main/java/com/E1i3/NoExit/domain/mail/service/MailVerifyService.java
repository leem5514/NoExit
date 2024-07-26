package com.E1i3.NoExit.domain.mail.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.E1i3.NoExit.domain.mail.dto.MailReqDto;
import com.E1i3.NoExit.domain.common.service.RedisService;

@Service
public class MailVerifyService {

	private final JavaMailSender mailSender;
	private final RedisService redisService;

	private static final String AUTH_CODE_PREFIX = "USER_AUTH_CODE ";

	@Autowired
	public MailVerifyService(JavaMailSender mailSender, RedisService redisService) {
		this.mailSender = mailSender;
		this.redisService = redisService;
	}

	// 	인증번호 생성
	public String createCode(){
		StringBuilder sb = new StringBuilder();	//인증번호
		try{
			Random random = SecureRandom.getInstanceStrong();
			for (int i = 0; i < 6; i++) {
				// 6자리 랜덤 수 생성 -> 추후에 문자와 숫자 섞는걸로 수정
				sb.append(random.nextInt(10));
			}
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// 메일 전송
	public void sendEmail(MailReqDto mailReqDto) {
		// 전송할 이메일 저장
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(mailReqDto.getReceiver());
		message.setSubject(mailReqDto.getTitle());
		message.setText(mailReqDto.getContents());
		mailSender.send(message);	//이메일 전송
	}

	// 	인증번호 검증
	// 이메일을 입력받아 redis의 인증코드와 파라미터의 인증코드가 동일한지 비교
	// 일치하면 true, 불일치하면 false;
	public boolean verifiedCode(String email, String authCode) {
		// 	존재하는 회원 정보가 있는지 확인
		String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
		return redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
	}


}
