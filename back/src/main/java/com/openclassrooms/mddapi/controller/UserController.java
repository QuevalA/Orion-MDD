package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {

        UserDTO user = userService.getUserDTOById(id);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/subscribe/{topicId}")
    public ResponseEntity<String> subscribeToTopic(@PathVariable Long userId, @PathVariable Long topicId) {
        try {
            userService.subscribeToTopic(userId, topicId);
            return ResponseEntity.ok("Subscribed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{userId}/unsubscribe/{topicId}")
    public ResponseEntity<String> unsubscribeFromTopic(@PathVariable Long userId, @PathVariable Long topicId) {
        if (userService.unsubscribeFromTopic(userId, topicId)) {
            return ResponseEntity.ok("Unsubscribed successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
