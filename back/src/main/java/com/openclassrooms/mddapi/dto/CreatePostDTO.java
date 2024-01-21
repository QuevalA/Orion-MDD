package com.openclassrooms.mddapi.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreatePostDTO {

    @NotNull
    @Size(max = 80)
    private String title;

    @NotNull
    @Size(max = 10000)
    private String content;

    @NotNull
    private Long topicId;
}
