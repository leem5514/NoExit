package com.E1i3.NoExit.domain.common.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthFilter extends GenericFilter {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String bearerToken = ((HttpServletRequest)request).getHeader("Authorization");
		try {
			if (bearerToken != null){
				if(!bearerToken.substring(0, 7).equals("Bearer ")){
					throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
				}
				String token = bearerToken.substring(7);

				if (token != null){
					Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

					List<GrantedAuthority> authorities = new ArrayList<>();
					authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
					UserDetails userDetails = new User(claims.getSubject(), "", authorities);
					Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}else {
					System.out.println("token null");
				}

			}
			chain.doFilter(request, response);

		} catch (Exception e){
			log.error(e.getMessage());
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			httpServletResponse.setContentType("application/json");
			httpServletResponse.getWriter().write("token error");
		}
	}
}
