package com.E1i3.NoExit.domain.chat.domain;

import java.security.Principal;

// jwt -> channelInterceptor
public class StompPrincipal implements Principal {
    private String email;

    public StompPrincipal(String email) {
        this.email = email;
    }

    @Override
    public String getName() {
        return email;
    }
}
