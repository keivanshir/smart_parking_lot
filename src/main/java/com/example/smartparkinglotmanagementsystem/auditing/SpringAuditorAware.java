package com.example.smartparkinglotmanagementsystem.auditing;

import com.example.smartparkinglotmanagementsystem.entity.User;
import com.example.smartparkinglotmanagementsystem.security.AuthUser;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringAuditorAware implements AuditorAware<AuthUser> {
    @Override
    @Nonnull
    public Optional<AuthUser> getCurrentAuditor() {
        Optional<Object> principal = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal);

        if (principal.isPresent()){
            String principalString = principal.get().toString();
            return principalString.equals("anonymousUser") ?
                    Optional.of(AuthUser.builder()
                            .user(User.builder()
                                    .name("anonymousUser")
                                    .build())
                            .build())
                    : principal.map(AuthUser.class::cast);
        } else {
            return Optional.of(AuthUser.builder().build());
        }
    }
}
