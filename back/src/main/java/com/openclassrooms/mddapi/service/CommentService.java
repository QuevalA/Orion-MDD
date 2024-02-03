package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentDTO;
import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final TopicService topicService;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository,
                          UserService userService,
                          TopicService topicService,
                          PostService postService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.topicService = topicService;
        this.postService = postService;
    }

    @Override
    public CommentDTO createComment(CreateCommentDTO createCommentDTO) {
        Comment newComment = convertCreateCommentDTOToEntity(createCommentDTO);

        Comment savedComment = commentRepository.save(newComment);

        return convertCommentEntityToDto(savedComment);
    }

    @Override
    public List<CommentDTO> getCommentsByPost(Long id) {
        List<Comment> comments = commentRepository.findByPostId(id);

        return comments.stream()
                .map(this::convertCommentEntityToDto)
                .collect(Collectors.toList());
    }

    private CommentDTO convertCommentEntityToDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setCommentAuthor(comment.getCommentAuthor() != null ? comment.getCommentAuthor().getId() : null);
        commentDTO.setPost(comment.getPost() != null ? comment.getPost().getId() : null);

        if (comment.getCommentAuthor() != null) {
            commentDTO.setCommentAuthorUsername(comment.getCommentAuthor().getUsername());
        }

        return commentDTO;
    }

    private Comment convertCreateCommentDTOToEntity(CreateCommentDTO createCommentDTO) {
        Comment comment = new Comment();

        //Retrieve authenticated User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        comment.setContent(createCommentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(postService.getPostEntityById(createCommentDTO.getPostId()));

        comment.setCommentAuthor(userService.getUserEntityById(userId));

        return comment;
    }
}
