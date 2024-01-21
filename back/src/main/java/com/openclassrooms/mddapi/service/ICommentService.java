package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentDTO;

import java.util.List;

public interface ICommentService {
    CommentDTO createComment(CreateCommentDTO createCommentDTO);
    List<CommentDTO> getCommentsByPost(Long id);
}
