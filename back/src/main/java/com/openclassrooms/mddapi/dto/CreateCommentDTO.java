package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class CreateCommentDTO {

    private String content;
    private Long postId;
}
