package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubPost;
import com.anabada.neighbor.club.repository.ClubRepository;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public ClubPost clubPostList() {
        ClubPost result = new ClubPost();
        List<Post> post = clubRepository.clubPostList();
        result.setPostId(post.get(0).getPostId());
        result.setContent(post.get(0).getContent());
        return result;
    }
}
