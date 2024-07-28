package com.E1i3.NoExit.domain.common.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonErrorDto {
	private int status_code;
	private String error_message;

	public CommonErrorDto(HttpStatus httpStatus, String error_message) {
		this.status_code = httpStatus.value();
		this.error_message = error_message;
	}
}
