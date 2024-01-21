package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentDTO;
import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.repository.CommentRepository;
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
    
    private CommentDTO convertCommentEntityToDto(Comment comment){
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setCommentAuthor(comment.getCommentAuthor() != null ? comment.getCommentAuthor().getId() : null);
        commentDTO.setPost(comment.getPost() != null ? comment.getPost().getId() : null);

        return commentDTO;
    }

    private Comment convertCreateCommentDTOToEntity(CreateCommentDTO createCommentDTO) {
        Comment comment = new Comment();

        comment.setContent(createCommentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(postService.getPostEntityById(createCommentDTO.getPostId()));
        //Fixed User id 1 until security implementation
        comment.setCommentAuthor(userService.getUserEntityById(1L));

        return comment;
    }
}
