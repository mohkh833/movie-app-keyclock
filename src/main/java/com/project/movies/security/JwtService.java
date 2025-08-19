package com.project.movies.security;
import com.project.movies.config.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class JwtService {

    private final KeycloakProperties keycloakProperties;


    public String getFullName(String token) {
        return decodeJwt(token).getClaimAsString("name");
    }

    public String getUserRole(String token) {
        Jwt jwt = decodeJwt(token);
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");

        if (roles != null) {
            return roles.stream()
                    .filter(r -> r.equals("USER") || r.equals("ADMIN"))
                    .findFirst()
                    .orElse("UNKNOWN");
        }
        return "UNKNOWN";
    }

    public List<String> getUserRoles(String token) {
        Jwt jwt = decodeJwt(token);

        List<String> roles = new ArrayList<>();

        // Extract realm roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            roles.addAll((List<String>) realmAccess.get("roles"));
        }

        // Extract resource (client) roles
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            for (Object clientObj : resourceAccess.values()) {
                if (clientObj instanceof Map) {
                    Map<String, Object> clientRoles = (Map<String, Object>) clientObj;
                    if (clientRoles.containsKey("roles")) {
                        roles.addAll((List<String>) clientRoles.get("roles"));
                    }
                }
            }
        }

        return roles.isEmpty() ? Collections.singletonList("UNKNOWN") : roles;
    }

    public Jwt decodeJwt(String token) {
        String jwkSetUri = keycloakProperties.getAuthServerUrl()
                + "/realms/" + keycloakProperties.getRealm()
                + "/protocol/openid-connect/certs";
        JwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        return decoder.decode(token);
    }
}
