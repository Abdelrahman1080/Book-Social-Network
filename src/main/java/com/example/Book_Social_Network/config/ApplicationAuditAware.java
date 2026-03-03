package com.example.Book_Social_Network.config;

import com.example.Book_Social_Network.common.BaseEntity;
import com.example.Book_Social_Network.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Integer> {


    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return Optional.of(((User) principal).getId());
            }
        }
        return Optional.empty();
    }
}
