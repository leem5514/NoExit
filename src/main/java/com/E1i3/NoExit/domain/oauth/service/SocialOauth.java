package com.E1i3.NoExit.domain.oauth.service;

import org.springframework.http.ResponseEntity;

import com.E1i3.NoExit.domain.oauth.dto.GoogleOAuthToken;
import com.E1i3.NoExit.domain.oauth.dto.UserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface SocialOauth {

	String getOauthRedirectURL();

	ResponseEntity<String> requestAccessToken(String code);

	GoogleOAuthToken getAccessToken(ResponseEntity<String> accessToken) throws JsonProcessingException;

	ResponseEntity<String> requestUserInfo(GoogleOAuthToken googleOAuthToken);

	UserInfoDto getUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException;

}