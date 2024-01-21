package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final TopicRepository topicRepository;
	private final TopicService topicService;
	private final UserService userService;

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
		Post newPost = convertCreatePostDTOToEntity(createPostDTO);

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

	PostDTO convertPostEntityToDto(Post post){
		PostDTO postDTO = new PostDTO();

		postDTO.setId(post.getId());
		postDTO.setTitle(post.getTitle());
		postDTO.setContent(post.getContent());
		postDTO.setCreatedAt(post.getCreatedAt());

		postDTO.setTopic(post.getTopic() != null ? post.getTopic().getId() : null);

		postDTO.setPostAuthor(post.getPostAuthor() != null ? post.getPostAuthor().getId() : null);

		postDTO.setComments(post.getComments() != null
				? post.getComments().stream().map(Comment::getId).collect(Collectors.toList())
				: Collections.emptyList());

		return postDTO;
	}

	private Post convertCreatePostDTOToEntity(CreatePostDTO createPostDTO) {
		Post post = new Post();

		post.setTitle(createPostDTO.getTitle());
		post.setContent(createPostDTO.getContent());
		post.setCreatedAt(LocalDateTime.now());
		post.setTopic(topicService.getTopicEntityById(createPostDTO.getTopicId()));
		//Fixed User id 1 until security implementation
		post.setPostAuthor(userService.getUserEntityById(1L));
		post.setComments(Collections.emptyList());

		return post;
	}
}
