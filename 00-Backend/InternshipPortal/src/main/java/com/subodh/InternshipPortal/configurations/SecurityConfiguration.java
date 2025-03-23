package com.subodh.InternshipPortal.configurations;


import com.subodh.InternshipPortal.filters.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The type Security configuration.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    /**
     * The Jwt filter.
     */
    final JwtFilter jwtFilter ;

    /**
     * Instantiates a new Security configuration.
     *
     * @param jwtFilter the jwt filter
     */
    public SecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/api/v1/public/**")
                                .permitAll()
                                .requestMatchers("/api/v1/common/**").hasAnyRole("ADMIN", "STUDENT","INSTRUCTOR")
                                .requestMatchers("/api/v1/students/**").hasRole("STUDENT")
                                .requestMatchers("/api/v1/instructors/**").hasRole("INSTRUCTOR")
                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
