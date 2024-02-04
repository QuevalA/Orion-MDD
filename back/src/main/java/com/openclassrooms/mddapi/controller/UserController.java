package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.dto.UserUpdateDTO;
import com.openclassrooms.mddapi.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller class responsible for handling user-related HTTP requests.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private IUserService userService;

    /**
     * Constructor for UserController.
     *
     * @param userService the UserService implementation
     */
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return ResponseEntity containing the user information if found, otherwise returns a 404 response
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {

        UserDTO user = userService.getUserDTOById(id);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Subscribes a user to a topic.
     *
     * @param userId  the ID of the user
     * @param topicId the ID of the topic
     * @return ResponseEntity indicating the subscription status
     */
    @PostMapping("/{userId}/subscribe/{topicId}")
    public ResponseEntity<String> subscribeToTopic(@PathVariable Long userId, @PathVariable Long topicId) {
        try {
            userService.subscribeToTopic(userId, topicId);
            return ResponseEntity.ok("Subscribed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Unsubscribes a user from a topic.
     *
     * @param userId  the ID of the user
     * @param topicId the ID of the topic
     * @return ResponseEntity indicating the unsubscription status
     */
    @PostMapping("/{userId}/unsubscribe/{topicId}")
    public ResponseEntity<String> unsubscribeFromTopic(@PathVariable Long userId, @PathVariable Long topicId) {
        if (userService.unsubscribeFromTopic(userId, topicId)) {
            return ResponseEntity.ok("Unsubscribed successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates user credentials.
     *
     * @param requestBody the request body containing user credentials
     * @return ResponseEntity indicating the update status
     */
    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String email = requestBody.get("email");

        UserUpdateDTO updatedUser = userService.updateUserCredentials(username, email);

        if (updatedUser != null) {
            return ResponseEntity.ok("User credentials updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
