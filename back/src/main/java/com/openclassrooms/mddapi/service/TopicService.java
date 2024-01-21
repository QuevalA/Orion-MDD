package com.openclassrooms.mddapi.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.openclassrooms.mddapi.dto.SubscribedTopicDTO;
import com.openclassrooms.mddapi.dto.TopicDTO;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.repository.TopicRepository;

@Service
public class TopicService implements ITopicService {
	private final TopicRepository topicRepository;
	private final UserService userService;

	public TopicService(TopicRepository topicRepository, UserService userService) {
		this.topicRepository = topicRepository;
		this.userService = userService;

	}

	@Override
	public List<TopicDTO> getTopics() {
		List<Topic> topics = topicRepository.findAll();

		return topics.stream().map(this::convertTopicEntityToDto).collect(Collectors.toList());
	}

	@Override
	public TopicDTO getTopicDTOById(Long id) {
		Topic topic = topicRepository.findById(id).orElse(null);

		return (topic != null) ? convertTopicEntityToDto(topic) : null;
	}

	public Topic getTopicEntityById(Long id) {

        return topicRepository.findById(id).orElse(null);
	}

	public List<SubscribedTopicDTO> getSubscribedTopicsByUserId(Long userId) {
		User user = userService.getUserEntityById(userId);

		if (user != null) {
			Set<Topic> subscribedTopics = user.getSubscribedTopics();

			return subscribedTopics.stream()
					.map(this::convertSubscribedTopicEntityToDto)
					.collect(Collectors.toList());
		}

		return Collections.emptyList();
	}

	private TopicDTO convertTopicEntityToDto(Topic topic){
		TopicDTO topicDTO = new TopicDTO();

		topicDTO.setId(topic.getId());
		topicDTO.setName(topic.getName());
		topicDTO.setDescription(topic.getDescription());
		topicDTO.setCreatedAt(topic.getCreatedAt());

		return topicDTO;
	}

	public Topic convertTopicDTOToEntity(TopicDTO topicDTO) {
		Topic topic = new Topic();

		topic.setId(topicDTO.getId());
		topic.setName(topicDTO.getName());
		topic.setDescription(topicDTO.getDescription());
		topic.setCreatedAt(topicDTO.getCreatedAt());

		return topic;
	}

	private SubscribedTopicDTO convertSubscribedTopicEntityToDto(Topic topic) {
		SubscribedTopicDTO subscribedTopicDTO = new SubscribedTopicDTO();

		subscribedTopicDTO.setId(topic.getId());
		subscribedTopicDTO.setName(topic.getName());
		subscribedTopicDTO.setDescription(topic.getDescription());

		return subscribedTopicDTO;
	}
}
