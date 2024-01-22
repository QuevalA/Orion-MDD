package com.openclassrooms.mddapi.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.payload.request.AuthRequest;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
        super.setAuthenticationManager(authenticationManager);
    }

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

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String token = JwtUtils.generateToken(userDetails);
        response.addHeader("Authorization", "Bearer " + token);
    }
}
