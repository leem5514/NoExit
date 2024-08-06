package com.E1i3.NoExit.domain.oauth.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.E1i3.NoExit.domain.oauth.dto.GoogleOAuthToken;
import com.E1i3.NoExit.domain.oauth.dto.UserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth{

	@Value("${spring.security.oauth2.client.registration.google.url}")	// 처음 접속하는 url
	private String GOOGLE_SNS_LOGIN_URL;

	@Value("${spring.security.oauth2.client.registration.google.client-id}")	// google-cloud에서 발급받은 client-id
	private String GOOGLE_SNS_CLIENT_ID;

	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")	// redirect할 url
	private String GOOGLE_SNS_CALLBACK_URL;

	@Value("${spring.security.oauth2.client.registration.google.client-secret}")	// google-cloud에서 발급받은 client-secret
	private String GOOGLE_SNS_CLIENT_SECRET;

	@Value("${spring.security.oauth2.client.registration.google.scope}")	// 발급받고자하는 정보(email, profile)
	private String GOOGLE_DATA_ACCESS_SCOPE;

	// Stirng 값을 객체로 바꾸는 Mapper
	private final ObjectMapper objectMapper;

	// // Google API로 요청을 보내고 받을 객체입니다.
	private final RestTemplate restTemplate;

	@Override
	public String getOauthRedirectURL() {
		Map<String,Object> params = new HashMap<>();
		params.put("scope",GOOGLE_DATA_ACCESS_SCOPE);
		params.put("response_type","code");
		params.put("client_id",GOOGLE_SNS_CLIENT_ID);
		params.put("redirect_uri",GOOGLE_SNS_CALLBACK_URL);

		String parameterString = params.entrySet().stream()
			.map(x -> x.getKey() + "=" + x.getValue())
			.collect(Collectors.joining("&"));
		String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
		log.info("redirect-URL={}", redirectURL);
		return redirectURL;
	}

	@Override
	public ResponseEntity<String> requestAccessToken(String code) {
		String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		params.put("client_id", GOOGLE_SNS_CLIENT_ID);
		params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
		params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
		params.put("grant_type", "authorization_code");
		ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL, params, String.class);

		return stringResponseEntity;
	}

	@Override
	public GoogleOAuthToken getAccessToken(ResponseEntity<String> accessToken) throws JsonProcessingException {
		log.info("accessTokenBody: {}",accessToken.getBody());
		return objectMapper.readValue(accessToken.getBody(),GoogleOAuthToken.class);
	}

	@Override
	public ResponseEntity<String> requestUserInfo(GoogleOAuthToken googleOAuthToken) {
		String GOOGLE_USERINFO_REQUEST_URL= "https://www.googleapis.com/oauth2/v2/userinfo";
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
		headers.add("Authorization", "Bearer " + googleOAuthToken.getAccess_token());
		ResponseEntity<String> exchange = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
		System.out.println(exchange.getBody());
		return exchange;
	}

	@Override
	public UserInfoDto getUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException {
		UserInfoDto userInfoDto = objectMapper.readValue(userInfoResponse.getBody(), UserInfoDto.class);
		return userInfoDto;
	}

}
