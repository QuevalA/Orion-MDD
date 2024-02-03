package com.openclassrooms.mddapi.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> authoredPosts;
    private List<Long> authoredComments;
    private Set<Long> subscribedTopics;
}
