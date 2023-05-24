package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.club.domain.Club;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository {
    public int clubPostInsert(Post post);

    public int clubInsert(Club club);

    public Long hobbySelect(String hobbyName);

    public List<Post> clubPostList();

    public Club clubList(long postId);

    public String findHobbyName(long hobbyId);

}
