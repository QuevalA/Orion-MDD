package com.openclassrooms.mddapi.security.jwt;

import com.openclassrooms.mddapi.security.services.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final CustomUserDetailsService customUserDetailsService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService) {

        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = (CustomUserDetailsService) userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Extract and validate the JWT token from the Authorization header

         String token = extractToken(request);
         if (token != null && JwtUtils.validateToken(token)) {
             String username = JwtUtils.getUsernameFromToken(token);
             UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

             // Authenticate the user
             UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                     userDetails, null, userDetails.getAuthorities()
             );
             SecurityContextHolder.getContext().setAuthentication(authenticationToken);
         }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract the token excluding "Bearer "
        }

        return null;
    }
}
