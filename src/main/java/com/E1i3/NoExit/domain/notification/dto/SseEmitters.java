package com.E1i3.NoExit.domain.notification.dto;

import com.E1i3.NoExit.domain.member.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SseEmitters {
	private String email;
	private Role role;
}
