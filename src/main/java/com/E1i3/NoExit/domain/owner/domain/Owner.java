package com.E1i3.NoExit.domain.owner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.E1i3.NoExit.domain.common.domain.BaseTimeEntity;
import com.E1i3.NoExit.domain.member.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.owner.dto.OwnerListResDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerSaveReqDto;
import com.E1i3.NoExit.domain.owner.dto.OwnerUpdateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Owner extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false)
	private String username;

	@Column(length = 255, nullable = false)
	private String password;

	@Column(length = 50, nullable = false)
	private String storeName;

	@Column(length = 100, unique = true)
	private String email;

	@Column(length = 255, nullable = false)
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Role role = Role.OWNER;

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private DelYN delYN = DelYN.N;

	public Owner updateOwner(OwnerUpdateDto dto, String encodedPassword) {
		this.username = dto.getUsername();
		this.password =  encodedPassword;
		return this;
	}

	public Owner updateDelYN() {
		this.delYN = DelYN.Y;
		return this;
	}

	public OwnerListResDto fromEntity(){
		return OwnerListResDto.builder()
			.id(this.id)
			.username(this.username)
			.storeName(this.storeName)
			.build();
	}

}
