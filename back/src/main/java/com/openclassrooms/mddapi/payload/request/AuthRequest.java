package com.openclassrooms.mddapi.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class AuthRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
