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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private TopicRepository topicRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

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

    @Override
    public UserDTO getUserDTOById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        return (user != null) ? convertUserEntityToDto(user) : null;
    }

    public User getUserEntityById(Long id) {

        return userRepository.findById(id).orElse(null);
    }

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

    public UserUpdateDTO updateUserCredentials(Long userId, String emailOrUsername, String password) {
        // Retrieve the user entity from the database
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            if (emailOrUsername != null && !emailOrUsername.isEmpty()) {
                // Check if the provided value is an email
                if (isValidEmail(emailOrUsername)) {
                    user.setEmail(emailOrUsername);
                } else {
                    user.setUsername(emailOrUsername);
                }
            }

            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }

            user = userRepository.save(user);

            return convertUserEntityToUpdateDto(user);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    private boolean isValidEmail(String input) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(input);

        // Return true if the email matches the pattern, false otherwise
        return matcher.matches();
    }

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

    public UserUpdateDTO convertUserEntityToUpdateDto(User user) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

        userUpdateDTO.setId(user.getId());
        userUpdateDTO.setEmail(user.getEmail());
        userUpdateDTO.setUsername(user.getUsername());

        return userUpdateDTO;
    }

    public User convertUserDTOToEntity(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());

        List<Post> authoredPosts = userDTO.getAuthoredPosts().stream()
                .map(postId -> postRepository.findById(postId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        user.setAuthoredPosts(authoredPosts);

        List<Comment> authoredComments = userDTO.getAuthoredComments().stream()
                .map(commentId -> commentRepository.findById(commentId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        user.setAuthoredComments(authoredComments);

        Set<Topic> subscribedTopics = userDTO.getSubscribedTopics().stream()
                .map(topicId -> topicRepository.findById(topicId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        user.setSubscribedTopics(subscribedTopics);

        return user;
    }
}
