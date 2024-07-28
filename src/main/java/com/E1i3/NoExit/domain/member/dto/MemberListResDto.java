package com.E1i3.NoExit.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberListResDto {
	private Long id;
	private String username;
	private String email;
	private String nickname;
}
