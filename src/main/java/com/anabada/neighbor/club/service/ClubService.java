package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubPost;
import com.anabada.neighbor.club.domain.entity.Hobby;
import com.anabada.neighbor.used.domain.Post;

public interface ClubService {
    public void clubPostList();

    public int clubSave(ClubPost clubPost);//clubSave

    public long clubPostSave(ClubPost post);//생성된 게시글의 postId 반환

    public int findByHobbyId(String hobbyName);//hobbyName으로 hobbyId찾기

    public int clubPostCheck(ClubPost clubPost);//clubPost객체의 Null값체크

}
