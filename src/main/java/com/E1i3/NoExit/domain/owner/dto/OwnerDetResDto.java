package com.E1i3.NoExit.domain.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDetResDto {
	private String username;
	private String storeName;
	private String email;
}
