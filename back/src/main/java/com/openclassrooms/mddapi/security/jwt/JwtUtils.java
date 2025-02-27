package com.openclassrooms.mddapi.security.jwt;

import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for JWT token generation, validation, and extraction.
 */
@Component
public class JwtUtils {

    private static final byte[] SECRET_KEY_BYTES = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
    private static final long JWT_EXPIRATION_MS = 864000000; // 10 days

    /**
     * Generates a JWT token for the given UserDetails.
     *
     * @param userDetails the UserDetails object representing the authenticated user
     * @return the generated JWT token
     */
    public static String generateToken(UserDetails userDetails) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;

        return Jwts.builder()
                .setSubject(userDetailsImpl.getEmail())
                .claim("userId", userDetailsImpl.getId())
                .claim("username", userDetailsImpl.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY_BYTES)
                .compact();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY_BYTES).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY_BYTES).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
