package com.E1i3.NoExit.domain.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
	// at로 사용자 정보 요청 후 반환하는 dto
	public String id;
	public String email;
	public Boolean verifiedEmail;
	public String name;
	public String givenName;
	public String familyName;
	public String picture;
	public String locale;
}
