package com.openclassrooms.mddapi.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegistrationRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String username;
}
