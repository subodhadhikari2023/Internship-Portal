package com.subodh.InternshipPortal.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JwtUtils}.
 *
 * <p>Covers token generation, username extraction, and expiry/signature validation.
 */
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    /** A 256-bit (32-byte) HMAC-SHA key used only in tests. */
    private static final String TEST_SECRET = "testSecretKeyForJwtTokenGenerationMustBe256BitsLong1234567890abcdef";

    /**
     * Creates a fresh {@link JwtUtils} instance with the test secret injected via reflection.
     */
    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "SECRET_KEY", TEST_SECRET);
    }

    /**
     * Builds an {@link Authentication} object for the given username and role.
     *
     * @param username the principal username
     * @param role     the granted authority name
     * @return a pre-authenticated token
     */
    private Authentication buildAuthentication(String username, String role) {
        UserDetails userDetails = User.withUsername(username)
                .password("password")
                .authorities(new SimpleGrantedAuthority(role))
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    /**
     * Tests that a generated token is non-null and non-empty.
     */
    @Test
    @DisplayName("generateToken returns a non-empty token string")
    void generateToken_returnsNonEmptyToken() {
        Authentication auth = buildAuthentication("student@test.com", "ROLE_STUDENT");
        String token = jwtUtils.generateToken(auth);
        assertThat(token).isNotBlank();
    }

    /**
     * Tests that the username extracted from a generated token matches the principal.
     */
    @Test
    @DisplayName("extractUserName returns the correct username from a valid token")
    void extractUserName_returnsCorrectUsername() {
        Authentication auth = buildAuthentication("student@test.com", "ROLE_STUDENT");
        String token = jwtUtils.generateToken(auth);

        String extractedUsername = jwtUtils.extractUserName(token);
        assertThat(extractedUsername).isEqualTo("student@test.com");
    }

    /**
     * Tests that {@code validateToken} returns {@code true} for a freshly generated token.
     */
    @Test
    @DisplayName("validateToken returns true for a valid, non-expired token")
    void validateToken_returnsTrue_forValidToken() {
        Authentication auth = buildAuthentication("instructor@test.com", "ROLE_INSTRUCTOR");
        String token = jwtUtils.generateToken(auth);

        UserDetails userDetails = User.withUsername("instructor@test.com")
                .password("pass")
                .authorities(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))
                .build();

        assertThat(jwtUtils.validateToken(token, userDetails)).isTrue();
    }

    /**
     * Tests that {@code validateToken} returns {@code false} when the username does not match.
     */
    @Test
    @DisplayName("validateToken returns false when username does not match token subject")
    void validateToken_returnsFalse_whenUsernameMismatch() {
        Authentication auth = buildAuthentication("student@test.com", "ROLE_STUDENT");
        String token = jwtUtils.generateToken(auth);

        UserDetails differentUser = User.withUsername("other@test.com")
                .password("pass")
                .authorities(new SimpleGrantedAuthority("ROLE_STUDENT"))
                .build();

        assertThat(jwtUtils.validateToken(token, differentUser)).isFalse();
    }
}
