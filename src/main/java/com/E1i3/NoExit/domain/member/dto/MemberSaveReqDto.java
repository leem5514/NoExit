package com.E1i3.NoExit.domain.member.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveReqDto {
	// 회원가입 정보 기입
	private String username;
	private String password;
	private String email;

	private int age;
	// private Role role;

	private String phone_number;
	private String nickname;

	private LocalDateTime createdTime;
	private LocalDateTime updateTime;
}

