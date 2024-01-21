package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.SubscribedTopicDTO;
import com.openclassrooms.mddapi.dto.TopicDTO;

public interface ITopicService {
	List<TopicDTO> getTopics();

	TopicDTO getTopicDTOById(Long id);
	List<SubscribedTopicDTO> getSubscribedTopicsByUserId(Long userId);
}
