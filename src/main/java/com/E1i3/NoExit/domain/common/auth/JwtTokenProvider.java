package com.E1i3.NoExit.domain.common.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	@Value("${jwt.secretKey}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private int expiration;

	@Value("${jwt.secretKeyRt}")
	private String secretKeyRt;

	@Value("${jwt.expirationRt}")
	private int expirationRt;

	public String createToken(String email, String role) {
		// claims는 사용자 정보(payload 정보)
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("role", role);

		Date now =  new Date();
		String token = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)    //생성시간
			.setExpiration(new Date(now.getTime() + (expiration * 60 * 1000L)))  // 만료시간 30분
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
		return token;
	}

	public String createRefreshToken(String email, String role) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("role", role);
		Date now =  new Date();
		String token = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)    //생성 시간
			.setExpiration(new Date(now.getTime() + expirationRt * 60 * 1000L))
			.signWith(SignatureAlgorithm.HS256, secretKeyRt)
			.compact();
		return token;
	}
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
		UserDetails userDetails = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
//		public String getNicknameFromToken(String token) {
//			try {
//				Claims claims = Jwts.parser()
//						.setSigningKey(secretKey) // 서명 키 설정
//						.parseClaimsJws(token) // 토큰 파싱 및 클레임 추출
//						.getBody();
//
//				String email = claims.get(subject);
//				System.out.println("Extracted nickname from token: " + email); // 로그 추가
//				return nickname;
//			} catch (Exception e) {
//				throw new RuntimeException("Invalid token", e);
//			}
//		}

}
