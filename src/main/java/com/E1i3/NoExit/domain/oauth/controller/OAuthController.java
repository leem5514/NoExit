package com.E1i3.NoExit.domain.oauth.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.E1i3.NoExit.domain.oauth.service.OAuthService;
import com.E1i3.NoExit.domain.oauth.dto.SocialOAuthResDto;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
// @RequestMapping(value = "/login/oauth2", produces = "application/json")
@RequestMapping("/app/accounts")
public class OAuthController {

	private final OAuthService oAuthService;

	public OAuthController(OAuthService oAuthService) {
		this.oAuthService = oAuthService;
	}

	// http://localhost:8080/app/accounts/auth/google/redirect
	@GetMapping("/google/redirect")
	public void googleLogin(@RequestParam String code) {
		String registrationId = "google";
		oAuthService.socialLogin(code, registrationId);
	}

	// 요청을 Google 로그인을 할 수 있도록 리디렉션 해주는 핸들러
	@GetMapping("/auth/{type}")
	public void socialLoginRequest(@PathVariable("type")String type, HttpServletResponse response) throws IOException {
		String requestURL = oAuthService.request(type.toUpperCase());
		response.sendRedirect(requestURL);
	}

	//
	@GetMapping("/auth/{type}/redirect")
	public ResponseEntity<?> callback(@PathVariable(name = "type") String type,
		@RequestParam(name = "code") String code) throws JsonProcessingException {
		SocialOAuthResDto socialOAuthResDto = oAuthService.oAuthLogin(code);
		return new ResponseEntity<>(socialOAuthResDto, HttpStatus.OK);
	}
}
