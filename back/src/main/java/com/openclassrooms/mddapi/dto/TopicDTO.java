package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class TopicDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Set<Long> subscribers;
}
