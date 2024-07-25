package com.E1i3.NoExit.domain.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.E1i3.NoExit.domain.member.dto.MailReqDto;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@Builder
public class MailService {
	@Autowired
	private final JavaMailSender mailSender;

	// 메일 전송
	public void sendEmail(MailReqDto mailReqDto) {
		// 전송할 이메일 저장
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(mailReqDto.getReceiver());
		message.setSubject(mailReqDto.getTitle());
		message.setText(mailReqDto.getContents());
		mailSender.send(message);	//이메일 전송
	}
}
