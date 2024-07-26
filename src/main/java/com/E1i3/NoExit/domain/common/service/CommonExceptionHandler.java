package com.E1i3.NoExit.domain.common.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.E1i3.NoExit.domain.common.dto.CommonErrorDto;

@ControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<CommonErrorDto> entityNotFoundHandler(EntityNotFoundException e) {
		CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
		return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<CommonErrorDto> illegalArgumentHandler(IllegalArgumentException e) {
		CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
		return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonErrorDto> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
		CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, "argument is not valid");
		return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonErrorDto> ExceptionHandler(Exception e) {
		CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "error!!!!!!!!!!!!");
		return new ResponseEntity<>(commonErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
