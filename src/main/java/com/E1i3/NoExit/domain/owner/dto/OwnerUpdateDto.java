package com.E1i3.NoExit.domain.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerUpdateDto {
	// 회원정보 수정
	private String username;
	private String password;
	private String email;
	private String storeName;
}