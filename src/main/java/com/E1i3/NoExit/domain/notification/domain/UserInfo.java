package com.E1i3.NoExit.domain.notification.domain;

import com.E1i3.NoExit.domain.member.domain.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
	private Role role;
	private String email;
}
