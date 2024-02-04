package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.payload.request.AuthRequest;
import com.openclassrooms.mddapi.payload.request.RegistrationRequest;
import com.openclassrooms.mddapi.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * Controller class for handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint for user registration.
     *
     * @param registrationRequest The registration request containing user details.
     * @return ResponseEntity with the result of the registration process.
     */
    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authService.registerUser(registrationRequest);
    }

    /**
     * Endpoint for user login.
     *
     * @param authRequest The authentication request containing user credentials.
     * @return ResponseEntity with the result of the login process.
     */
    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        return authService.loginUser(authRequest);
    }

    /**
     * Endpoint for user logout.
     *
     * @return ResponseEntity with the result of the logout process.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return authService.logoutUser();
    }
}
