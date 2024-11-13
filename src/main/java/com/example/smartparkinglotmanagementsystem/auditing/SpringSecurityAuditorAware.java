package com.example.smartparkinglotmanagementsystem.auditing;

import com.example.smartparkinglotmanagementsystem.security.AuthUser;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<AuthUser> {
    @Override
    @Nonnull
    public Optional<AuthUser> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(AuthUser.class::cast);
    }
}
