package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentDTO;
import com.openclassrooms.mddapi.service.ICommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller class for managing comments.
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    private ICommentService commentService;

    /**
     * Constructs a new CommentController with the specified comment service.
     *
     * @param commentService The comment service to use.
     */
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Handles the creation of a new comment.
     *
     * @param createCommentDTO The DTO containing the data for creating the comment.
     * @return ResponseEntity containing the created comment DTO.
     */
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody @Valid CreateCommentDTO createCommentDTO) {
        CommentDTO createdComment = commentService.createComment(createCommentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    /**
     * Retrieves the list of comments for a specified post.
     *
     * @param id The ID of the post to retrieve comments for.
     * @return The list of comment DTOs associated with the post.
     */
    @GetMapping("/{id}")
    public List<CommentDTO> getCommentsByPost(@PathVariable Long id) {
        return commentService.getCommentsByPost(id);
    }
}
