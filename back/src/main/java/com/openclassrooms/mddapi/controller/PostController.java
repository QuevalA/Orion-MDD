package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.service.IPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller class for managing posts.
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private IPostService postService;

    /**
     * Constructs a new PostController with the specified post service.
     *
     * @param postService The post service to use.
     */
    public PostController(IPostService postService) {
        this.postService = postService;
    }

    /**
     * Creates a new post.
     *
     * @param createPostDTO The data for creating the post.
     * @return The created post with HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody @Valid CreatePostDTO createPostDTO) {
        PostDTO createdPost = postService.createPost(createPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    /**
     * Retrieves posts by topic subscriptions.
     *
     * @param topicIds The IDs of the topics to filter posts by.
     * @return The list of posts filtered by topic subscriptions.
     */
    @GetMapping
    public List<PostDTO> getPostsBySubscriptions(@RequestParam List<Long> topicIds) {
        return postService.getPostsBySubscriptions(topicIds);
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param id The ID of the post to retrieve.
     * @return The post with the specified ID if found, otherwise returns HTTP status 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {

        PostDTO post = postService.getPostDTOById(id);

        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
