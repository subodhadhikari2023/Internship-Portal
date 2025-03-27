package com.subodh.InternshipPortal.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.subodh.InternshipPortal.exceptions.InvalidJWTTokenException;
import com.subodh.InternshipPortal.services.Implementation.UserDetailsServiceImplementation;
import com.subodh.InternshipPortal.utils.JwtUtils;
import com.subodh.InternshipPortal.wrapper.Response;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The type Jwt filter.
 */
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final ApplicationContext context;
    private final JwtUtils jwtUtils;


    /**
     * Instantiates a new Jwt filter.
     *
     * @param jwtUtils the jwt utils
     * @param context  the context
     */
    @Autowired
    public JwtFilter(JwtUtils jwtUtils, ApplicationContext context) {

        this.context = context;
        this.jwtUtils = jwtUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ExpiredJwtException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String userName = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                userName = jwtUtils.extractUserName(token);
            }
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = context.getBean(UserDetailsServiceImplementation.class).loadUserByUsername(userName);
                if (jwtUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            handleException(response, "JWT token has expired. Please login again.", HttpStatus.UNAUTHORIZED);
        } catch (JwtException ex) {
            handleException(response, "Invalid JWT token.", HttpStatus.UNAUTHORIZED);
        }
    }


    private void handleException(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        Response<String> errorResponse = new Response<>(message, status);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        response.getWriter().flush();
    }

}
