package com.openclassrooms.mddapi.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.payload.request.AuthRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Custom JWT authentication filter.
 */
@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * Constructs a new JwtAuthenticationFilter.
     *
     * @param authenticationManager the authentication manager
     * @param jwtUtils              the JWT utility class
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        super.setAuthenticationManager(authenticationManager);
    }

    /**
     * Attempt to authenticate the user using the credentials provided in the request.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the authenticated user's authentication object
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Read and parse JSON from the request body
            ObjectMapper objectMapper = new ObjectMapper();
            AuthRequest authRequest = objectMapper.readValue(request.getInputStream(), AuthRequest.class);

            // Create an authentication token
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword(), new ArrayList<>())
            );

            return authentication;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
