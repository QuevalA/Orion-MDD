package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.dto.UserUpdateDTO;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Service class for handling user-related operations.
 */
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private TopicRepository topicRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    /**
     * Constructs a new UserService with the required repositories and password encoder.
     *
     * @param userRepository    The user repository.
     * @param topicRepository   The topic repository.
     * @param postRepository    The post repository.
     * @param commentRepository The comment repository.
     * @param passwordEncoder   The password encoder.
     */
    public UserService(UserRepository userRepository,
                       TopicRepository topicRepository,
                       PostRepository postRepository,
                       CommentRepository commentRepository,
                       BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves a UserDTO by its ID.
     *
     * @param id The ID of the user.
     * @return The UserDTO if found, or null if not found.
     */
    @Override
    public UserDTO getUserDTOById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        return (user != null) ? convertUserEntityToDto(user) : null;
    }

    /**
     * Retrieves a User entity by its ID.
     *
     * @param id The ID of the user.
     * @return The User entity if found, or null if not found.
     */
    public User getUserEntityById(Long id) {

        return userRepository.findById(id).orElse(null);
    }

    /**
     * Subscribes a user to a topic.
     *
     * @param userId  The ID of the user.
     * @param topicId The ID of the topic.
     * @return true if subscribed successfully, false otherwise.
     * @throws RuntimeException if user or topic not found, or user is already subscribed to the topic.
     */
    public boolean subscribeToTopic(Long userId, Long topicId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            Topic topic = topicRepository.findById(topicId).orElse(null);

            if (topic != null) {
                boolean alreadySubscribed = user.getSubscribedTopics().contains(topic);

                if (!alreadySubscribed) {
                    user.getSubscribedTopics().add(topic);
                    userRepository.save(user);
                    return true;
                } else {
                    throw new RuntimeException("User with ID " + userId +
                            " is already subscribed to the topic with ID " +
                            topicId + ".");
                }
            } else {
                throw new RuntimeException("Topic with ID " + topicId + " not found.");
            }
        } else {
            throw new RuntimeException("User with ID " + userId + " not found.");
        }
    }

    /**
     * Unsubscribes a user from a topic.
     *
     * @param userId  The ID of the user.
     * @param topicId The ID of the topic.
     * @return true if unsubscribed successfully, false otherwise.
     */
    public boolean unsubscribeFromTopic(Long userId, Long topicId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            Topic topicToRemove = user.getSubscribedTopics().stream()
                    .filter(t -> t.getId().equals(topicId))
                    .findFirst()
                    .orElse(null);

            if (topicToRemove != null) {
                user.getSubscribedTopics().remove(topicToRemove);
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    /**
     * Updates user credentials.
     *
     * @param username The new username.
     * @param email    The new email.
     * @return The updated user details.
     * @throws RuntimeException if user not found or email is invalid.
     */
    public UserUpdateDTO updateUserCredentials(String username, String email) {
        //Retrieve authenticated User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // Retrieve the user entity from the database
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            if (username != null && !username.isEmpty()) {
                user.setUsername(username);
            }

            if (email != null && !email.isEmpty()) {
                if (isValidEmail(email)) {
                    user.setEmail(email);
                }
            }

            user = userRepository.save(user);

            return convertUserEntityToUpdateDto(user);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    /**
     * Checks if the given email is valid.
     *
     * @param input The email to validate.
     * @return true if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String input) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(input);

        // Return true if the email matches the pattern, false otherwise
        return matcher.matches();
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserDTO.
     */
    UserDTO convertUserEntityToDto(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());

        userDTO.setAuthoredPosts(user.getAuthoredPosts().stream()
                .map(Post::getId)
                .collect(Collectors.toList()));

        userDTO.setAuthoredComments(user.getAuthoredComments().stream()
                .map(Comment::getId)
                .collect(Collectors.toList()));

        userDTO.setSubscribedTopics(user.getSubscribedTopics().stream()
                .map(Topic::getId)
                .collect(Collectors.toSet()));

        return userDTO;
    }

    /**
     * Converts a User entity to a UserUpdateDTO.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserUpdateDTO.
     */
    public UserUpdateDTO convertUserEntityToUpdateDto(User user) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

        userUpdateDTO.setId(user.getId());
        userUpdateDTO.setUsername(user.getUsername());
        userUpdateDTO.setEmail(user.getEmail());

        return userUpdateDTO;
    }
}
