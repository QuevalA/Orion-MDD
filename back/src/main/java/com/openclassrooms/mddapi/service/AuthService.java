package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.request.AuthRequest;
import com.openclassrooms.mddapi.payload.request.RegistrationRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import com.openclassrooms.mddapi.security.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class responsible for handling authentication-related operations.
 */
@Service
public class AuthService implements IAuthService {


    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(CustomUserDetailsService userDetailsService,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       UserRepository userRepository,
                       AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user.
     *
     * @param registrationRequest The registration request containing user details.
     * @return ResponseEntity with the result of the registration process.
     */
    @Override
    public ResponseEntity<?> registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        User user = User.builder()
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .username(registrationRequest.getUsername())
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtUtils.generateToken(userDetails);

        AuthResponse authenticationResponse = new AuthResponse(token);

        return ResponseEntity.ok(authenticationResponse);
    }

    /**
     * Authenticates a user.
     *
     * @param authRequest The authentication request containing user credentials.
     * @return ResponseEntity with the result of the login process.
     */
    @Override
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication;
            UserDetails userDetails;

            // Check if the input matches an email format
            if (isValidEmail(authRequest.getEmail())) {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
                );
                userDetails = (UserDetails) authentication.getPrincipal();
            } else {
                // Attempt to authenticate using the username
                User user = userRepository.findByUsername(authRequest.getEmail())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authRequest.getEmail()));

                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getEmail(), authRequest.getPassword())
                );
                userDetails = (UserDetails) authentication.getPrincipal();
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateToken(userDetails);

            AuthResponse authenticationResponse = new AuthResponse(token);

            return ResponseEntity.ok(authenticationResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    /**
     * Validates if the provided input is a valid email address.
     *
     * @param input The input string to validate.
     * @return True if the input is a valid email address, false otherwise.
     */
    private boolean isValidEmail(String input) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(input);

        // Return true if the email matches the pattern, false otherwise
        return matcher.matches();
    }

    /**
     * Logs out the currently authenticated user.
     *
     * @return ResponseEntity with the result of the logout process.
     */
    @Override
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }
}
