package ca.gbc.comp3095.apigateway;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Test-only ReactiveJwtDecoder to avoid network calls to Keycloak during tests.
 */
@TestConfiguration
public class TestJwtDecoderConfig {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return token -> Mono.just(
                Jwt.withTokenValue(token)
                        .header("alg", "none")
                        .subject("test-subject")
                        .claim("preferred_username", "test-user")
                        .claim("realm_access", Map.of("roles", List.of("staff", "student")))
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(3600))
                        .build()
        );
    }
}
