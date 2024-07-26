package com.E1i3.NoExit.domain.member.dto;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveReqDto {
	// 회원가입 정보 기입
	private String username;
	private String password;
	private String email;

	private int age;
	private String phoneNumber;
	private String nickname;

	public Member toEntity(){
		return Member.builder()
			.username(this.username)
			.password(this.password)
			.email(this.email)
			.email(this.email)
			.age(this.age)
			.phoneNumber(this.phoneNumber)
			.nickname(this.nickname)
			.build();
	}
}

