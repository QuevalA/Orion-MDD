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

/**
 * JWT authorization filter to validate and authenticate JWT tokens in incoming requests.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final CustomUserDetailsService customUserDetailsService;

    private final AuthenticationManager authenticationManager;

    /**
     * Constructs a new JwtAuthorizationFilter.
     *
     * @param authenticationManager the authentication manager
     * @param userDetailsService    the custom user details service
     */
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService) {

        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = (CustomUserDetailsService) userDetailsService;
    }

    /**
     * Performs the JWT token validation and sets the authentication in the security context.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param chain    the filter chain
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
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

    /**
     * Extracts the JWT token from the Authorization header.
     *
     * @param request the HTTP request
     * @return the JWT token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract the token excluding "Bearer "
        }

        return null;
    }
}
