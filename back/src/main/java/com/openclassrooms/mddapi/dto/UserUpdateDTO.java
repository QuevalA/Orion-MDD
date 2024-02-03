package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserUpdateDTO {

    private Long id;
    private String email;
    private String username;
    private String password;
    private LocalDateTime updatedAt;
}
