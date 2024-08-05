package com.E1i3.NoExit.domain.member.dto;

import org.springframework.web.multipart.MultipartFile;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Builder // Loader 사용을 위한 임시 추가. : 김민성
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

	// Loader 사용을 위한 임시 추가. : 김민성
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Role role = Role.USER;

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

