package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {

    private Long id;
    private String title;
    private String content;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;
    private Long topic;
    private Long postAuthor;
    private List<Long> comments;
}
