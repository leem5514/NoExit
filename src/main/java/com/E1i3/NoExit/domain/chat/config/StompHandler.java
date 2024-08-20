package com.E1i3.NoExit.domain.chat.config;

import com.E1i3.NoExit.domain.common.auth.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@RequiredArgsConstructor
//@Component
//public class StompHandler implements ChannelInterceptor {
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        if (accessor.getCommand() == StompCommand.CONNECT) {
//            String token = accessor.getFirstNativeHeader("Authorization");
//            if (token != null && token.startsWith("Bearer ")) {
//                token = token.substring(7);
//            } else {
//                throw new AccessDeniedException("Invalid Authorization header");
//            }
//
//            if (!jwtTokenProvider.validateToken(token)) {
//                throw new AccessDeniedException("Invalid JWT token");
//            }
//
//            Claims claims = jwtTokenProvider.getClaimsFromToken(token);
//            String username = claims.getSubject();
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
//            UserDetails userDetails = new User(username, "", authorities);
//            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            accessor.setUser(authentication);
//        }
//        return message;
//    }
//}