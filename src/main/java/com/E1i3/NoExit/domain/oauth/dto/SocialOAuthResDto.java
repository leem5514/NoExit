package com.E1i3.NoExit.domain.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialOAuthResDto {
	// 사용자 인증 후 받아온 at
	private String jwtToken;
	private int user_num;
	private String accessToken;
	private String tokenType;
}
