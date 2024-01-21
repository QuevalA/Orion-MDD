package com.openclassrooms.mddapi.controller;

import java.util.List;

import com.openclassrooms.mddapi.dto.SubscribedTopicDTO;
import com.openclassrooms.mddapi.dto.TopicDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.service.ITopicService;

@RestController
@RequestMapping("/topics")
public class TopicController {
	private ITopicService topicService;
	
	public TopicController(ITopicService topicService) {
		this.topicService = topicService;		
	}

	@GetMapping
	public List<TopicDTO> getTopics() {
		return topicService.getTopics();
	}

	@GetMapping("/{id}")
	public ResponseEntity<TopicDTO> getTopicById(@PathVariable Long id) {
		TopicDTO topic = topicService.getTopicDTOById(id);

		if (topic != null) {
			return ResponseEntity.ok(topic);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/subscribed/{userId}")
	public ResponseEntity<List<SubscribedTopicDTO>> getSubscribedTopicsByUserId(@PathVariable Long userId) {
		List<SubscribedTopicDTO> subscribedTopics = topicService.getSubscribedTopicsByUserId(userId);

		if (!subscribedTopics.isEmpty()) {
			return ResponseEntity.ok(subscribedTopics);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
