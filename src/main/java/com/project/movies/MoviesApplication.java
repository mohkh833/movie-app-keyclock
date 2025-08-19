package com.project.movies;

import com.project.movies.config.KeycloakProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(KeycloakProperties.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class MoviesApplication {

	private final KeycloakProperties keycloakProperties;

	public MoviesApplication(KeycloakProperties keycloakProperties) {
		this.keycloakProperties = keycloakProperties;
	}

	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println("✅ Keycloak Realm: " + keycloakProperties.getRealm());
		System.out.println("✅ Keycloak Client Secret: " + keycloakProperties.getCredentials().getSecret());
		System.out.println("✅ Keycloak Admin Username: " + keycloakProperties.getAdmin().getUsername());
	}
}
