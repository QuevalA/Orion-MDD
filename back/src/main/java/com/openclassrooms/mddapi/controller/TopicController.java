package com.openclassrooms.mddapi.controller;

import java.util.List;

import com.openclassrooms.mddapi.dto.SubscribedTopicDTO;
import com.openclassrooms.mddapi.dto.TopicDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.service.ITopicService;

/**
 * Controller class for managing topics.
 */
@RestController
@RequestMapping("/topics")
public class TopicController {
    private ITopicService topicService;

    /**
     * Constructs a new TopicController with the specified topic service.
     *
     * @param topicService The topic service to use.
     */
    public TopicController(ITopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Retrieves all topics.
     *
     * @return The list of topics.
     */
    @GetMapping
    public List<TopicDTO> getTopics() {
        return topicService.getTopics();
    }

    /**
     * Retrieves a topic by its ID.
     *
     * @param id The ID of the topic.
     * @return The topic if found, or 404 Not Found response otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable Long id) {
        TopicDTO topic = topicService.getTopicDTOById(id);

        if (topic != null) {
            return ResponseEntity.ok(topic);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves the topics subscribed by a user.
     *
     * @param userId The ID of the user.
     * @return The list of subscribed topics if found, or 404 Not Found response otherwise.
     */
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
