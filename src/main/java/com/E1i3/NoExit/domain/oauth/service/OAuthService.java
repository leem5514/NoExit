package com.E1i3.NoExit.domain.oauth.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.E1i3.NoExit.domain.oauth.dto.GoogleOAuthToken;
import com.E1i3.NoExit.domain.oauth.dto.SocialOAuthResDto;
import com.E1i3.NoExit.domain.oauth.dto.UserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class OAuthService {

	private final SocialOauth socialOauth;

	public OAuthService(SocialOauth socialOauth) {
		this.socialOauth = socialOauth;
	}

	public void socialLogin(String code, String registrationId) {
		System.out.println("code: " + code);
		System.out.println("registrationId: " + registrationId);
	}

	// 리디렉션할 url 생성
	public String request(String type) throws IOException {
		String redirectURL;
		redirectURL = socialOauth.getOauthRedirectURL();
		return redirectURL;
	}

	// 인증 성공 후 해당 code로 at를 얻고 at를 통해서 사용자 정보 얻어오는 메서드
	public SocialOAuthResDto oAuthLogin(String code) throws  JsonProcessingException {
		ResponseEntity<String> accessToken = socialOauth.requestAccessToken(code);	// at 요청
		GoogleOAuthToken googleOAuthToken = socialOauth.getAccessToken(accessToken);	// at 발급
		ResponseEntity<String> userInfoResponse = socialOauth.requestUserInfo(googleOAuthToken); // 사용자 정보 요청
		UserInfoDto userInfoDto = socialOauth.getUserInfo(userInfoResponse);	// 사용자 정보 반환

		String userEmail = userInfoDto.getEmail();
		// 임의로 토큰 발급
		return new SocialOAuthResDto("1234",1,"asdf", googleOAuthToken.getToken_type());
	}

}
