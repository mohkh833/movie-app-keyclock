package com.project.movies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        // Return current username/email, or "system" if unauthenticated
        return () -> Optional.of("system");
    }
}
