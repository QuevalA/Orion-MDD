package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service class for managing posts.
 */
@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final TopicService topicService;
    private final UserService userService;

    /**
     * Constructs a new PostService with the specified repositories and services.
     *
     * @param postRepository  The post repository to use.
     * @param userRepository  The user repository to use.
     * @param topicRepository The topic repository to use.
     * @param topicService    The topic service to use.
     * @param userService     The user service to use.
     */
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       TopicRepository topicRepository,
                       TopicService topicService,
                       UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.topicService = topicService;
        this.userService = userService;
    }

    @Override
    public PostDTO createPost(CreatePostDTO createPostDTO) {
        Post newPost = convertPostDTOToEntity(createPostDTO);

        Post savedPost = postRepository.save(newPost);

        return convertPostEntityToDto(savedPost);
    }

    @Override
    public List<PostDTO> getPostsBySubscriptions(List<Long> topicIds) {
        List<Post> posts = postRepository.findByTopicIdIn(topicIds);

        // Convert the list of Post entities to a list of PostDTOs
        return posts.stream()
                .map(this::convertPostEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getPostDTOById(Long id) {
        Post post = postRepository.findById(id).orElse(null);

        return (post != null) ? convertPostEntityToDto(post) : null;
    }

    public Post getPostEntityById(Long id) {

        return postRepository.findById(id).orElse(null);
    }

    PostDTO convertPostEntityToDto(Post post) {
        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setCreatedAt(post.getCreatedAt());

        if (post.getTopic() != null) {
            postDTO.setTopic(post.getTopic().getId());
            postDTO.setTopicName(post.getTopic().getName());
        }

        if (post.getPostAuthor() != null) {
            postDTO.setPostAuthor(post.getPostAuthor().getId());
            postDTO.setPostAuthorUsername(post.getPostAuthor().getUsername());
        }

        postDTO.setComments(post.getComments() != null
                ? post.getComments().stream().map(Comment::getId).collect(Collectors.toList())
                : Collections.emptyList());

        return postDTO;
    }

    private Post convertPostDTOToEntity(CreatePostDTO createPostDTO) {
        Post post = new Post();

        //Retrieve authenticated User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        post.setTitle(createPostDTO.getTitle());
        post.setContent(createPostDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setTopic(topicService.getTopicEntityById(createPostDTO.getTopicId()));

        post.setPostAuthor(userService.getUserEntityById(userId));
        post.setComments(Collections.emptyList());

        return post;
    }
}
