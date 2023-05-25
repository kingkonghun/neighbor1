package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.club.domain.ClubPost;
import com.anabada.neighbor.club.domain.PostSave;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository {
    public int clubPostSave(PostSave postSave);

    public int clubSave(Club club);

    public long findByHobbyId(String hobbyName);

    public String findHobbyName(long hobbyId);

}
