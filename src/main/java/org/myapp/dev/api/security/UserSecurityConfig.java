package org.myapp.dev.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurityConfig")
public class UserSecurityConfig {

    public boolean hasAccess(Authentication authentication, String userId) {
        return (authentication.getName().equals(userId));
    }
}
