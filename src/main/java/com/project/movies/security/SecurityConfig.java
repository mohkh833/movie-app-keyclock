package com.project.movies.security;

import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) // Enables @Secured annotations
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .cors(Customizer.withDefaults()) // Enable CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/movies/**",
                                "/v2/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**"
                        ).permitAll() // Public endpoints
                        .anyRequest().authenticated() // Secure everything else
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())); // Keycloak JWT support

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = grantedAuthoritiesConverter.convert(jwt);

            var clientRoles = ((Map<String, Object>) jwt.getClaims()
                    .getOrDefault("resource_access", Map.of()))
                    .values().stream()
                    .flatMap(val -> ((Map<String, List<String>>) val)
                            .getOrDefault("roles", List.of()).stream())
                    .map(role -> "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            var combined = new ArrayList<GrantedAuthority>();
            if(authorities != null) combined.addAll(authorities);
            combined.addAll(clientRoles);
            return combined;
        });
        return jwtAuthenticationConverter;
    }
}
