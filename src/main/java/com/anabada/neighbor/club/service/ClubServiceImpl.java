package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubPost;
import com.anabada.neighbor.club.domain.PostSave;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.repository.ClubRepository;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public void clubPostList() {

    }

    @Override
    public int clubSave(ClubPost clubPost) {
        //생성된 게시글의 postId반환
        clubPostSave(clubPost);
//        if(){
//
//        }
        return 1;
    }

    //생성된 게시글의 postId반환
    @Override
    public long clubPostSave(ClubPost clubPost) {
        PostSave post = PostSave.builder().memberId(3)
                .title(clubPost.getTitle())
                .content(clubPost.getContent())
                .postType("club")
                .build();
        if (clubRepository.clubPostSave(post) == 1) {
            return post.getPostId();
        }else{
            return -1;
        }
    }

}
