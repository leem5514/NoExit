package com.E1i3.NoExit.domain.member.dto;

import org.springframework.web.multipart.MultipartFile;

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
	private String phone_number;
	private String nickname;

	public Member toEntity(String encodedPassword, String imageUrl){
		return Member.builder()
			.username(this.username)
			.password(encodedPassword)
			.email(this.email)
			.age(this.age)
			.profileImage(imageUrl)
			.phone_number(this.phone_number)
			.nickname(this.nickname)
			.build();
	}

}

