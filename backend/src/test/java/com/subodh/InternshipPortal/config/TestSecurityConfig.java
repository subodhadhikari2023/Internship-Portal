package com.subodh.InternshipPortal.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Test-only Spring Security configuration that permits all requests without JWT validation.
 *
 * <p>Imported by {@code @WebMvcTest} controller test classes that need to by-pass
 * the production JWT filter while still exercising Spring MVC dispatch.
 * Role-based access is asserted via {@code @WithMockUser} annotations on individual tests.
 */
@TestConfiguration
public class TestSecurityConfig {

    /**
     * Creates a permissive {@link SecurityFilterChain} that disables CSRF and allows all requests.
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the configured filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }
}
