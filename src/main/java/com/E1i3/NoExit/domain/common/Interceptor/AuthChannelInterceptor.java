package com.E1i3.NoExit.domain.common.Interceptor;

import com.E1i3.NoExit.domain.chat.domain.StompPrincipal;
import com.E1i3.NoExit.domain.common.auth.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    public AuthChannelInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            System.out.println("Access Token: " + accessToken);  // 로그 추가

            if (accessToken != null && accessToken.startsWith("Bearer ")) {
                accessToken = accessToken.substring(7);
                if (jwtTokenProvider.validateToken(accessToken)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Authentication set for: " + authentication.getName());

                    String email = authentication.getName(); // getName()으로 이메일을 가져옴
                    accessor.setUser(new StompPrincipal(email)); // 커스텀 Principal 사용
                } else {
                    throw new AuthenticationCredentialsNotFoundException("AccessToken is not valid");
                }
            } else {
                throw new AuthenticationCredentialsNotFoundException("AccessToken is missing");
            }
        }
        return message;
    }

    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}
