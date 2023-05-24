package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubPost;
import com.anabada.neighbor.used.domain.Post;

public interface ClubService {
    public void clubPostList();

    public int clubSave(ClubPost clubPost);

    //생성된 게시글의 postId 반환
    public long clubPostSave(ClubPost post);

//    public long club
}
