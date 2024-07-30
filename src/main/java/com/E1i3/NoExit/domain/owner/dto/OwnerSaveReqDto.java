package com.E1i3.NoExit.domain.owner.dto;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.owner.domain.Owner;
import com.E1i3.NoExit.domain.storeInfo.domain.StoreInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerSaveReqDto {
	// 회원가입 정보 기입
	private String username;
	private String password;
	private String storeName;
	private String email;
	private String phoneNumber;

	public Owner toEntity(String encodedPassword){
		return Owner.builder()
			.username(this.username)
			.password(encodedPassword)
			.storeName(this.storeName)
			.email(this.email)
			.build();
	}

	public StoreInfo toStoreInfoEntity() {
		return StoreInfo.builder()
			.storeName(this.storeName)
			.storeRating(0)
			.phoneNumber(this.phoneNumber)
			.build();
	}
}

