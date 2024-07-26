package com.E1i3.NoExit.domain.member.dto;

import com.E1i3.NoExit.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
	private String username;
	private String password;
	private String email;
	private int age;
	private String phone_number;
	private String nickname;
}