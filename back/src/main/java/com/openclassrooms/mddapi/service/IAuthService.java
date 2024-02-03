package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.payload.request.AuthRequest;
import com.openclassrooms.mddapi.payload.request.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<?> registerUser(RegistrationRequest registrationRequest);

    ResponseEntity<?> loginUser(AuthRequest authRequest);

    ResponseEntity<?> logoutUser();
}
