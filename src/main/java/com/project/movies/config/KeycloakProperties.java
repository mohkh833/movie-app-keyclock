package com.project.movies.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String realm;
    private String authServerUrl;
    private String resource;
    private Credentials credentials;
    private Admin admin;

    @Data
    public static class Credentials {
        private String secret;
    }

    @Data
    public static class Admin {
        private String username;
        private String password;
    }
}
