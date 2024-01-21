package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.dto.PostDTO;

import java.util.List;

public interface IPostService {

    PostDTO createPost(CreatePostDTO createPostDTO);

    List<PostDTO> getPostsBySubscriptions(List<Long> topicIds);

    PostDTO getPostDTOById(Long id);
}
