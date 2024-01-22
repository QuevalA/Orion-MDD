package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.request.AuthRequest;
import com.openclassrooms.mddapi.payload.request.RegistrationRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import com.openclassrooms.mddapi.security.services.CustomUserDetailsService;
import com.openclassrooms.mddapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        // Debug print
        System.out.println("Received registration request: " + registrationRequest);

        // Check if the email is already registered
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            // Debug print
            System.out.println("Email is already taken: " + registrationRequest.getEmail());

            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        // Debug print
        System.out.println("Email is available: " + registrationRequest.getEmail());

        // Create a new user
        User user = User.builder()
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .username(registrationRequest.getUsername())
                .build();

        // Debug print
        System.out.println("New user created: " + user);

        // Save the user to the database
        userRepository.save(user);

        // Debug print
        System.out.println("User saved to the database");

        // Load user details from custom user details service
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // Debug print
        System.out.println("User details loaded from custom user details service: " + userDetails);

        // Generate JWT token
        String token = jwtUtils.generateToken(user);

        // Debug print
        System.out.println("JWT token generated: " + token);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        // Debug print
        System.out.println("Received login request: " + authRequest);

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // Update the SecurityContext with the authenticated user
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails); // Corrected: use userDetails

            // Debug print
            System.out.println("JWT token generated for user: " + userDetails.getUsername());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Debug print
            System.out.println("Authentication failed: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    /*@GetMapping("/me")
    public ResponseEntity<String> me() {

    }*/
}
