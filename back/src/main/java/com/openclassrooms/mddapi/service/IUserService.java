package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.dto.UserUpdateDTO;

public interface IUserService {

    UserDTO getUserDTOById(Long id);

    boolean subscribeToTopic(Long userId, Long topicId);

    boolean unsubscribeFromTopic(Long userId, Long topicId);

    UserUpdateDTO updateUserCredentials(String username, String email);
}
